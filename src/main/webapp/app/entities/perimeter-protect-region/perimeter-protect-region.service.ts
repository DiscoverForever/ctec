import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { PerimeterProtectRegion } from './perimeter-protect-region.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PerimeterProtectRegionService {

    private resourceUrl =  SERVER_API_URL + 'api/perimeter-protect-regions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/perimeter-protect-regions';

    constructor(private http: Http) { }

    create(perimeterProtectRegion: PerimeterProtectRegion): Observable<PerimeterProtectRegion> {
        const copy = this.convert(perimeterProtectRegion);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(perimeterProtectRegion: PerimeterProtectRegion): Observable<PerimeterProtectRegion> {
        const copy = this.convert(perimeterProtectRegion);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<PerimeterProtectRegion> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to PerimeterProtectRegion.
     */
    private convertItemFromServer(json: any): PerimeterProtectRegion {
        const entity: PerimeterProtectRegion = Object.assign(new PerimeterProtectRegion(), json);
        return entity;
    }

    /**
     * Convert a PerimeterProtectRegion to a JSON which can be sent to the server.
     */
    private convert(perimeterProtectRegion: PerimeterProtectRegion): PerimeterProtectRegion {
        const copy: PerimeterProtectRegion = Object.assign({}, perimeterProtectRegion);
        return copy;
    }
}
