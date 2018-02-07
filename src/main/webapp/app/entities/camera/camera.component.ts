import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import {Jsonp, Response, BaseRequestOptions, URLSearchParams} from '@angular/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Camera } from './camera.model';
import { CameraService } from './camera.service';
import { AlarmRegion } from '../alarm-region/alarm-region.model';
import { AlarmRegionService } from '../alarm-region/alarm-region.service';
import { PerimeterProtectRegionService } from '../perimeter-protect-region/perimeter-protect-region.service';
import { PerimeterProtectRegion } from '../perimeter-protect-region/perimeter-protect-region.model';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper, createRequestOption } from '../../shared';
import * as JsMpeg from 'jsmpeg';
import * as Cropper from 'cropperjs';
import * as Screenfull from 'screenfull';
@Component({
    selector: 'jhi-camera',
    templateUrl: './camera.component.html',
    styleUrls: [
        'camera.component.scss'
    ]
})
export class CameraComponent implements OnInit, OnDestroy {

    private VIDEO_SERVER_URL = 'http://' + window.location.hostname + ':3000';
    private REQ_ID = 0;
    currentAccount: any;
    cameras: Camera[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    fullScreen: boolean;
    videos: Array<Video>;
    currentCropperId: number;
    cropperActionsVisiable: boolean;
    cropper: Cropper;

    constructor(
        private cameraService: CameraService,
        private alarmRegionService: AlarmRegionService,
        private perimeterProtectRegionService: PerimeterProtectRegionService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private elementRef: ElementRef,
        private jsonp: Jsonp
    ) {
        this.fullScreen = false;
        this.videos = [];
        this.cropperActionsVisiable = false;
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.cameraService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.cameraService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/camera'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/camera', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/camera', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCameras();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Camera) {
        return item.id;
    }
    registerChangeInCameras() {
        this.eventSubscriber = this.eventManager.subscribe('cameraListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    async initVideo(videoUrl: string, canvas: any, id: number) {
        const {client, player} = await this.initClientAndPlayer(videoUrl, canvas, id);
        client.addEventListener('open', () => {
            this.videos.push({id, player, client});
        });
        client.addEventListener('error', async(error) => {
            console.error('websocket视频解析服务出错', error);
            console.error('======尝试重连======');
            this.initClientAndPlayer(videoUrl, canvas, id);
        });
        client.addEventListener('message', function(event) {
            // console.log('Message from server', event.data);
        });
    }

    async initClientAndPlayer(videoUrl: string, canvas: any, id: number) {
        const res = await this.getVideoTsServer('rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov');
        const client = new WebSocket(res.data.wsUrl);
        const player = new JsMpeg(client, {canvas, autoplay: true});
        return {client, player};
    }

    playVideo(videoUrl: string, event: any, id: number) {
        const canvas = event.currentTarget.parentNode.parentNode.children[0].children[0]
        if (!this.videos.find((video) => video.id === id)) {
            this.initVideo(videoUrl, canvas, id);
        }
        // this.videos.find(video => video.id === id).player.play()
    }

    initCropper(img: HTMLImageElement) {
        this.cropper = new Cropper(img, {
            viewMode: 3,
            guides: true,
            background: false,
            autoCrop: true,
            checkCrossOrigin: false,
            zoomable: false,
            center: true,
            crop(e) {
                console.log(e.detail.x);
                console.log(e.detail.y);
                console.log(e.detail.width);
                console.log(e.detail.height);
                console.log(e.detail.rotate);
                console.log(e.detail.scaleX);
                console.log(e.detail.scaleY);
            }
        });
    }

    stopVideo(id: number) {
        const currentVideo = this.videos.find((video) => video.id === id);
        currentVideo.player.stop();
        this.videos = this.videos.filter((video) => video.id !== id);
    }

    /**
     * 退出全屏
     */
    cancleFullscreen() {
        Screenfull.exit();
    }
    requestFullscreen(el: HTMLElement) {
        if (Screenfull.enabled) {
            Screenfull.request(el);
        }
    }

    /**
     * 设置预警区域
     * @param {Camera} camera
     */
    setAlarmRegion(camera: Camera) {
        const position = this.cropper.getData();
        const alarmRegion = new AlarmRegion();
        alarmRegion.leftUpX = position.x;
        alarmRegion.leftUpY = position.y;
        alarmRegion.rightUpX = position.x + position.width;
        alarmRegion.rightUpY = position.y;
        alarmRegion.leftDownX = position.x;
        alarmRegion.leftDownY = position.y - position.height;
        alarmRegion.rightDownX = position.x + position.width;
        alarmRegion.rightDownY = position.y + position.height;
        if (camera.alarmRegion) {
            alarmRegion.id = camera.alarmRegion.id;
            this.alarmRegionService.update(alarmRegion);
        } else {
            this.alarmRegionService.create(alarmRegion).subscribe((res: AlarmRegion) => {
                camera.alarmRegion = res;
                this.cameraService.update(camera).subscribe();
            }, (error: Response) => {
                console.error(error)
            })
        }
    }

    /**
     * 设置周界防护区域
     * @param {Camera} camera
     */
    setPerimeterRegion(camera: Camera) {
        const position = this.cropper.getData();
        const perimeterProtectRegion = new PerimeterProtectRegion();
        perimeterProtectRegion.leftUpX = position.x;
        perimeterProtectRegion.leftUpY = position.y;
        perimeterProtectRegion.rightUpX = position.x + position.width;
        perimeterProtectRegion.rightUpY = position.y;
        perimeterProtectRegion.leftDownX = position.x;
        perimeterProtectRegion.leftDownY = position.y - position.height;
        perimeterProtectRegion.rightDownX = position.x + position.width;
        perimeterProtectRegion.rightDownY = position.y + position.height;
        if (camera.perimeterProtectRegion) {
            perimeterProtectRegion.id = camera.perimeterProtectRegion.id;
            this.perimeterProtectRegionService.update(perimeterProtectRegion);
        } else {
            this.perimeterProtectRegionService.create(perimeterProtectRegion).subscribe((res: PerimeterProtectRegion) => {
                camera.perimeterProtectRegion = res;
                this.cameraService.update(camera).subscribe();
            }, (error: Response) => {
                console.error(error)
            });
        }
    }
    cancleCropper() {
        this.cropperActionsVisiable = false;
        this.cropper.destroy();
    }

    /**
     * 获取视频服务url
     */
    getVideoTsServer(streamUrl: string) {
        const options: BaseRequestOptions = new BaseRequestOptions();
        const params: URLSearchParams = new URLSearchParams();
        params.set('callback', `__ng_jsonp__.__req${this.REQ_ID++}.finished`);
        params.set('streamUrl', streamUrl);
        options.params = params;
        return this.jsonp.get(`${this.VIDEO_SERVER_URL}/start_server`, options).map((res) => res.json()).toPromise();
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.cameras = data;
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
interface Video {
    id: number;
    player: any;
    client: any;
}
