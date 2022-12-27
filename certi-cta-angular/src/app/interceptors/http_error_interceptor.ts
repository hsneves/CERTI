import {
    HttpErrorResponse, HttpEvent,
    HttpHandler, HttpInterceptor, HttpRequest
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry, tap } from 'rxjs/operators';
import { eventBus } from '../app.module';
import { Constants } from '../utils/constants';
export class HttpErrorInterceptor implements HttpInterceptor {

    intercept(
        request: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        return next.handle(request)
            .pipe(
                retry(1),
                tap(data => {
                    // envia para o barramento a mensagem de sucesso
                    eventBus.cast(Constants.HTTP_INTERCEPTOR_REST, { error: false });
                }),
                catchError((error: HttpErrorResponse) => {
                    let errorMessage = '';
                    if (error.error instanceof ErrorEvent) {
                        // erro cliente
                        errorMessage = "Error: " + error.error.message;
                    } else {
                        // erro servidor
                        errorMessage = "Status do erro: " + error.status + "\nMessage: " + error.message;
                    }
                    // envia para o barramento a mensagem de erro
                    eventBus.cast(Constants.HTTP_INTERCEPTOR_REST, { error: true, message: errorMessage });
                    return throwError(() => {
                        return errorMessage;
                    });
                })
            )
    }
}