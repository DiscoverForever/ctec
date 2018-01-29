import { Component, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { Camera } from './camera.model';
import { CameraService } from './camera.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import * as JsMpeg from 'jsmpeg';
import * as Cropper from "cropperjs";
import * as Screenfull from 'screenfull';
@Component({
    selector: 'jhi-camera',
    templateUrl: './camera.component.html',
    styleUrls: [
        'camera.component.scss'
    ]
})
export class CameraComponent implements OnInit, OnDestroy {

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
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private elementRef: ElementRef
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

    initVideo(videoUrl: string, canvas: any, id: number) {
        const client = new WebSocket('ws://localhost:9999');
        const player = new JsMpeg(client, {canvas: canvas, autoplay: true});
        this.videos.push({id, player, client});

    }

    playVideo(videoUrl: string, event: any, id: number) {
        const canvas = event.currentTarget.parentNode.parentNode.children[0].children[0]
        if (!this.videos.find(video => video.id === id)) this.initVideo(videoUrl, canvas, id);
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
            crop: function(e) {
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
        const video = this.videos.find(video => video.id === id);
        video.player.stop();
        this.videos = this.videos.filter(video => video.id !== id);
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
    setAlarmRegion() {
        console.log(this.cropper.getData())
    }
    setPerimeterRegion() {

    }
    cancleCropper() {
        this.cropperActionsVisiable = false;
        this.cropper.destroy();
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
