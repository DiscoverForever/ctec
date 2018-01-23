import { BaseEntity } from './../../shared';

export const enum DeviceStatus {
    'RUNNING',
    'STOP',
    'OVERHAULING'
}

export const enum FilterType {
    'HAIRSTYLE',
    'SEX',
    'UPPERWEAR',
    'UNDERWEAR',
    'CARRY_THING',
    'SUNGLASS'
}

export class Camera implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public cameraID?: string,
        public cameraIP?: string,
        public belongServer?: string,
        public belongChannel?: string,
        public collectStandards?: string,
        public deviceStatus?: DeviceStatus,
        public fastRunWarn?: boolean,
        public peopleCountLimitWarn?: boolean,
        public crowdsGatherWarn?: boolean,
        public vigorouslyWavedWarn?: boolean,
        public fightWarn?: boolean,
        public abnormalActionWarn?: boolean,
        public fastRunWarnLimit?: number,
        public peopleCountWarnLimit?: number,
        public crowdsGatherWarnLimit?: number,
        public vigorouslyWavedWarnLimit?: number,
        public fightWarnLimit?: number,
        public abnormalActionWarnLimit?: number,
        public filterType?: FilterType,
        public alarmHistories?: BaseEntity[],
        public alarmRegions?: BaseEntity[],
    ) {
        this.fastRunWarn = false;
        this.peopleCountLimitWarn = false;
        this.crowdsGatherWarn = false;
        this.vigorouslyWavedWarn = false;
        this.fightWarn = false;
        this.abnormalActionWarn = false;
    }
}
