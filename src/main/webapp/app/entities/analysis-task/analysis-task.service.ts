import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { AnalysisTask } from './analysis-task.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AnalysisTaskService {

    private resourceUrl =  SERVER_API_URL + 'api/analysis-tasks';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/analysis-tasks';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(analysisTask: AnalysisTask): Observable<AnalysisTask> {
        const copy = this.convert(analysisTask);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(analysisTask: AnalysisTask): Observable<AnalysisTask> {
        const copy = this.convert(analysisTask);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<AnalysisTask> {
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
     * Convert a returned JSON object to AnalysisTask.
     */
    private convertItemFromServer(json: any): AnalysisTask {
        const entity: AnalysisTask = Object.assign(new AnalysisTask(), json);
        entity.createdAt = this.dateUtils
            .convertDateTimeFromServer(json.createdAt);
        entity.executionAt = this.dateUtils
            .convertDateTimeFromServer(json.executionAt);
        return entity;
    }

    /**
     * Convert a AnalysisTask to a JSON which can be sent to the server.
     */
    private convert(analysisTask: AnalysisTask): AnalysisTask {
        const copy: AnalysisTask = Object.assign({}, analysisTask);

        copy.createdAt = this.dateUtils.toDate(analysisTask.createdAt);

        copy.executionAt = this.dateUtils.toDate(analysisTask.executionAt);
        return copy;
    }
}
