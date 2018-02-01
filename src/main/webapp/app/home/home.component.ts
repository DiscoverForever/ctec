import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager, JhiParseLinks} from 'ng-jhipster';

import {Account, LoginModalService, Principal} from '../shared';
import {AlarmHistoryService} from '../entities/alarm-history/alarm-history.service';
import {AlarmHistory, AlarmType} from '../entities/alarm-history/alarm-history.model';
import {ResponseWrapper} from '../shared';
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
    links: any;
    totalItems: any;
    queryCount: any;
    alarmHistories: AlarmHistory[];
    modalRef: NgbModalRef;
    currentYear: number;
    @ViewChild('lineEchartsWrapper')
    lineEchartsWrapper: ElementRef;
    @ViewChild('pieEchartsWrapper')
    pieEchartsWrapper: ElementRef;

    constructor(private principal: Principal,
                private loginModalService: LoginModalService,
                private eventManager: JhiEventManager,
                private alarmHistoryService: AlarmHistoryService,
                private jhiAlertService: JhiAlertService,
                private parseLinks: JhiParseLinks) {
    }

    ngOnInit() {
        this.currentYear = new Date().getFullYear();
        this.getAlarmHistoryData();
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
        this.initLineEcharts();
        this.initPieEcharts();
    }

    /**
     * 初始化折线图
     */
    initLineEcharts() {
        const myChart = Echars.init(this.lineEchartsWrapper.nativeElement);

        const option = {
            title: {
                text: '报警次数统计(按周)',
                subtext: '',
                x: 'center'
            },
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
                data: ['快速运动预警', '人数超限预警', '人群聚集预警', '剧烈挥手预警', '打架预警', '异常动作预警'],
                x: 'left'
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
                    text: '报警次数'
                }
            ],
            series: [
                {
                    name: '快速运动预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('FAST_RUN_WARN')
                },
                {
                    name: '人数超限预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('PEOPLE_COUNT_LIMIT_WARN')
                },
                {
                    name: '人群聚集预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('CROWDS_GATHER_WARN')
                },
                {
                    name: '剧烈挥手预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('VIGOROUSLY_WAVED_WARN')
                },
                {
                    name: '打架预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('FIGHT_WARN')
                },
                {
                    name: '异常动作预警',
                    type: 'bar',
                    data: this.getLineEchartsOption('ABNORMAL_ACTION_WARN')
                }
            ]
        };
        console.log(option)
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    /**
     * 初始化饼状图
     */
    initPieEcharts() {
        const myChart = Echars.init(this.pieEchartsWrapper.nativeElement);

        const option = {
            title: {
                text: '按报警次数统计(按区域)',
                subtext: '纯属虚构',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: ['快速运动预警', '人数超限预警', '人群聚集预警', '剧烈挥手预警', '打架预警', '异常动作预警']
            },
            series: [
                {
                    name: '访问来源',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: [
                        {value: this.getPieEchartsOption('FAST_RUN_WARN'), name: '快速运动预警'},
                        {value: this.getPieEchartsOption('PEOPLE_COUNT_LIMIT_WARN'), name: '人数超限预警'},
                        {value: this.getPieEchartsOption('CROWDS_GATHER_WARN'), name: '人群聚集预警'},
                        {value: this.getPieEchartsOption('VIGOROUSLY_WAVED_WARN'), name: '剧烈挥手预警'},
                        {value: this.getPieEchartsOption('FIGHT_WARN'), name: '打架预警'},
                        {value: this.getPieEchartsOption('ABNORMAL_ACTION_WARN'), name: '异常动作预警'}
                    ],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

    getAlarmHistoryData() {
        this.alarmHistoryService.query().subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    getLineEchartsOption(alarmType: any): number[] {
        return Array.apply(null, Array(51)).map((item, index) => {
            const currentWeekStartTime = new Date(this.currentYear.toString()).getTime() + 7 * 86400000 * index;
            const currentWeekEndTime = new Date(this.currentYear.toString()).getTime() + 7 * 86400000 * (index + 1);

            const currentWeekAlarmHistories = [];
            this.alarmHistories.map((alarmHistory: AlarmHistory) => {
                const alarmTime = new Date(alarmHistory.time).getTime();

                if (alarmTime && alarmTime >= currentWeekStartTime && alarmTime < currentWeekEndTime && alarmHistory.alarmType === alarmType) {
                    console.log('alarmTime', alarmTime)
                    console.log('currentWeekStartTime', currentWeekStartTime)
                    console.log('currentWeekEndTime', currentWeekEndTime)
                    currentWeekAlarmHistories.push(alarmHistory);
                }
            });
            return currentWeekAlarmHistories.length;
        })
    }

    getPieEchartsOption(alarmType: any): number {
        let count = 0;
        this.alarmHistories.map((alarmHistory: AlarmHistory) => {
            if (alarmHistory.alarmType === alarmType) {
                count ++;
            }
        });
        return count;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.alarmHistories = data;
        this.initEcharts();

    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
