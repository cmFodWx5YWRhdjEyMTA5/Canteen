package com.video.aashi.canteen.Canteens.localDB;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GsonStringConverterFactoryWrapper extends Converter.Factory {
    private GsonConverterFactory converterFactory;

    public static GsonStringConverterFactoryWrapper create() {
        return create(new Gson());
    }

    @SuppressWarnings("ConstantConditions")
    public static GsonStringConverterFactoryWrapper create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new GsonStringConverterFactoryWrapper(gson);
    }

    private final Gson gson;

    private GsonStringConverterFactoryWrapper(Gson gson) {
        this.gson = gson;
        converterFactory = GsonConverterFactory.create(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return converterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return converterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Nullable
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new GsonStringConverter<>(gson);
    }

    public static class GsonStringConverter<T> implements Converter<T, String> {
        private final Gson gson;

        GsonStringConverter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public String convert(@NonNull T value) throws IOException {
            return gson.toJson(value);
        }
    }
}