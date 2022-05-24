package com.example.myapplication.base.network

import java.lang.reflect.Type
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

// Retrofit directly throws an exception instead of parsing empty response bodies to null JSON. This converter handles that case.
class EmptyToNullConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val converter = retrofit.nextResponseBodyConverter<Any?>(this, type, annotations)
        return Converter<ResponseBody, Any?> { if (it.contentLength() == 0L) null else converter.convert(it) }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return retrofit.nextRequestBodyConverter<Any?>(this, type, parameterAnnotations, methodAnnotations)
    }
}
