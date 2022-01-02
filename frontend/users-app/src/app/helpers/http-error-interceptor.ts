import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpResponse,
    HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';

export class HttpErrorInterceptor implements HttpInterceptor {

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
            .pipe(
                catchError((error: HttpErrorResponse) => {
                    let errorMessage = '';
                    if (error.error instanceof ErrorEvent) {
                        // client-side error
                        errorMessage = error.error.message;
                    } else {
                        // server-side error
                        if (error.error.message) {
                            errorMessage = error.error.message;
                        } else {
                            errorMessage = error.message;
                        }
                    }
                    Swal.fire('VIAL Plus', errorMessage, 'error');
                    return throwError(errorMessage);
                })
            );
    }
}
