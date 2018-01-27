import { BaseEntity } from './../../shared';

export class PerimeterProtectRegion implements BaseEntity {
    constructor(
        public id?: number,
        public leftUpX?: number,
        public leftUpY?: number,
        public rightUpX?: number,
        public rightUpY?: number,
        public leftDownX?: number,
        public leftDownY?: number,
        public rightDownX?: number,
        public rightDownY?: number,
    ) {
    }
}
