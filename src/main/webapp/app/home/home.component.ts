import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import * as Cropper from 'cropperjs';
import * as Echars from 'echarts';
@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    @ViewChild('echartsWrapper')
    echartsWrapper: ElementRef
    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.initEcharts();
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    /**
     * 初始化echarts
     */
    initEcharts() {
        const myChart = Echars.init(this.echartsWrapper.nativeElement);

        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                data:['快速运动预警','人数超限预警','人群聚集预警','剧烈挥手预警','打架预警','异常动作预警']
            },
            xAxis: [
                {
                    type: 'category',
                    data: Array.apply(null, Array(51)).map((item, index) => `${index + 1}周`),
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                }
            ],
            series: [
                {
                    name:'快速运动预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                },
                {
                    name:'人数超限预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                },
                {
                    name:'人群聚集预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                },
                {
                    name:'剧烈挥手预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                },
                {
                    name:'打架预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                },
                {
                    name:'异常动作预警',
                    type:'bar',
                    data:Array.apply(null, Array(51)).map(() => Math.random())
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
}
