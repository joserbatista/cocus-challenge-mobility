package com.cocus.mobility.challenge.controller

import com.cocus.mobility.challenge.entity.ErrorEntity
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalErrorWebExceptionHandler(
    errorAttributes: GlobalErrorAttributes, applicationContext: ApplicationContext, serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(errorAttributes, WebProperties.Resources(), applicationContext) {

    init {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse?> {
        return RouterFunctions.route(RequestPredicates.all(), ::renderErrorResponse)
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse?> {
        val options =
            ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.MESSAGE).including(ErrorAttributeOptions.Include.EXCEPTION)
        val errorAttributes = this.getErrorAttributes(request, options)
        return ServerResponse.status(errorAttributes["statusCode"] as HttpStatus).contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(errorAttributes))

    }

    @Component
    class GlobalErrorAttributes : DefaultErrorAttributes() {

        override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any?> {
            val errorAttributes = super.getErrorAttributes(request, options)
            val status = errorAttributes["status"] ?: 500
            return ErrorEntity(HttpStatus.valueOf(status as Int), errorAttributes["message"] as String?).toMap()

        }
    }

}
