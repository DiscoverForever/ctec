import { BaseEntity } from './../../shared';

export const enum AlarmType {
    'FAST_RUN_WARN',
    'PEOPLE_COUNT_LIMIT_WARN',
    'CROWDS_GATHER_WARN',
    'VIGOROUSLY_WAVED_WARN',
    'FIGHT_WARN',
    'ABNORMAL_ACTION_WARN'
}

export class AlarmHistory implements BaseEntity {
    constructor(
        public id?: number,
        public imageContentType?: string,
        public image?: any,
        public alarmType?: AlarmType,
        public time?: any,
        public camera?: BaseEntity,
    ) {
    }
}
