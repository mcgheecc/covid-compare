import { HttpClient } from '@angular/common/http';
import { Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { StatusMessage } from 'src/app/model/status-message';
import { environment } from '../../environments/environment'

const BASE_URL = environment.BACKEND_HOST + '/batch-jobs/status';
const URL = {
  BATCH_JOB_STATUS: (jobName: string) => `${BASE_URL}/${jobName}`,
};

@Injectable({
  providedIn: 'root'
})
export class BatchJobStatusService {

  constructor(private httpClient: HttpClient ) {

  }

  getJobStatus(jobName: string): Observable<StatusMessage> {
    return this.httpClient.get<StatusMessage>(`${URL.BATCH_JOB_STATUS(jobName)}`);
  }
}
