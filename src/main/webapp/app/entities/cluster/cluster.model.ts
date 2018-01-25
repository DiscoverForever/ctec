import { BaseEntity } from './../../shared';

export class Cluster implements BaseEntity {
    constructor(
        public id?: number,
        public videoServerIp?: string,
        public videoChannelNumber?: number,
        public videoServerPort?: number,
        public videoServerUsername?: string,
        public videoServerPassword?: string,
        public dbIp?: string,
        public dbName?: string,
        public clusterNodeIp?: string,
        public clusterNodeName?: string,
    ) {
    }
}
