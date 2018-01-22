import { BaseEntity } from './../../shared';

export const enum AnalysisType {
    'BEHAVIOR',
    'STRUCTURED',
    'PERIMETER_PROTECT'
}

export const enum Priority {
    'HIGHT',
    'MIDDLE',
    'LOW'
}

export const enum AnalysisStatus {
    'ON_ANALYSIS',
    'ON_PAUSE'
}

export const enum VideoType {
    'LOCAL',
    'REALTIME'
}

export class AnalysisTask implements BaseEntity {
    constructor(
        public id?: number,
        public analysisType?: AnalysisType,
        public priority?: Priority,
        public analysisStatus?: AnalysisStatus,
        public videoType?: VideoType,
        public createdAt?: any,
        public executionAt?: any,
    ) {
    }
}
