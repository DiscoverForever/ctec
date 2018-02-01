import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageHelper, JhiTrackerService } from '../../shared';
import {AlarmHistory} from '../../entities/alarm-history/alarm-history.model';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {
    @ViewChild('alarmModal')
    alarmModal: ElementRef;
    @ViewChild('alarmAudio')
    alarmAudio: ElementRef;
    alarmHistory: AlarmHistory;
    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private jhiTrackerService: JhiTrackerService,
        private modalService: NgbModal,
        private router: Router
    ) {}

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'ctecApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
        this.jhiTrackerService.subscribe('/topic/alarm');
        this.jhiTrackerService.receive().subscribe((message) => {
            console.log('警报信息', message);
            this.alarmHistory = message;
            this.playAlarmAudio();
            this.openModal(this.alarmModal).then((result) => {
                this.stopAlarmAudio();
            });
        });
    }

    openModal(dialog: ElementRef) {
        return this.modalService.open(dialog, { size: 'lg' }).result;
    }

    playAlarmAudio() {
        this.alarmAudio.nativeElement.play();
    }

    stopAlarmAudio() {
        this.alarmAudio.nativeElement.pause();
    }
}
