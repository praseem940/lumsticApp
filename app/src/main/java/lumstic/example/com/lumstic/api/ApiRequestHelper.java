package lumstic.example.com.lumstic.api;




import org.codehaus.jackson.map.ObjectMapper;

import java.net.HttpRetryException;
import java.util.Map;

import lumstic.example.com.lumstic.LumsticApp;
import lumstic.example.com.lumstic.Utils.Logger;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

public class ApiRequestHelper {

    public static interface onRequestComplete {
        public void onSuccess(Object object);
        public void onFailure(ApiResponse apiResponse);
    }

    private static ApiRequestHelper instance;
    private LumsticService lumsticService;
    private LumsticApp application;

    public static synchronized ApiRequestHelper init(LumsticApp application) {
        if (null == instance) {
            instance = new ApiRequestHelper();
            instance.setApplication(application);
            instance.createRestAdapter();
        }
        return instance;
    }

    /**
     * Add all the api request's here
     */

//    public void getAdvertisements(final onRequestComplete onRequestComplete) {
//        lumsticService.getAdvertisements(new Callback<ApiResponse>() {
//            @Override
//            public void success(ApiResponse apiResponse, Response response) {
//                if (apiResponse.isSuccess()) {
//                    Map<String, Object> dataMap = (Map<String, Object>) apiResponse.getData();
//                    onRequestComplete.onSuccess(DataMapParser.parseAdvertisements(dataMap));
//                } else {
//                    onRequestComplete.onFailure(apiResponse);
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                onRequestComplete.onFailure(new ApiResponse().setError(ApiResponse.ApiError.COMMUNICATION_ERROR));
//            }
//        });
//    }
//
//    public void registerUser(String emailAddress, String phoneNumber, final onRequestComplete onRequestComplete) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail(emailAddress);
//        userDTO.setPhoneNumber(phoneNumber);
//
//        lumsticService.registerUser(userDTO, new Callback<ApiResponse>() {
//            @Override
//            public void success(ApiResponse apiResponse, Response response) {
//                if (apiResponse.isSuccess()) {
//                    Map<String, Object> responseMap = (Map<String, Object>) apiResponse.getData();
//                    onRequestComplete.onSuccess(DataMapParser.parseAuthToken(responseMap));
//                } else {
//                    onRequestComplete.onFailure(apiResponse);
//                }
//            }
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                onRequestComplete.onFailure(new ApiResponse().setError(ApiResponse.ApiError.COMMUNICATION_ERROR));
//            }
//        });
//    }
//
//    public void getPlaceResults(Place place, String searchLogId, final onRequestComplete onRequestComplete) {
//        lumsticService.getPlaceResults(place.getPlaceId(), place.getLatitude(), place.getLongitude(), place.getName(), searchLogId, new Callback<ApiResponse>() {
//            public void success(ApiResponse apiResponse, Response response) {
//                if (apiResponse.isSuccess()) {
//
//                } else {
//                    onRequestComplete.onFailure(apiResponse);
//                }
//            }
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                onRequestComplete.onFailure(new ApiResponse().setError(ApiResponse.ApiError.COMMUNICATION_ERROR));
//            }
//        });
//    }
//
//
//    public void getSearchResults(double latitude, double longitude, final onRequestComplete onRequestComplete) {
//        lumsticService.getSearchResults(String.valueOf(latitude), String.valueOf(longitude), new Callback<ApiResponse>() {
//            @Override
//            public void success(ApiResponse apiResponse, Response response) {
//                if (apiResponse.isSuccess()) {
//                    Map<String, Object> dataMap = (Map<String, Object>) apiResponse.getData();
//                    String searchLogId = dataMap.get("searchLog_id").toString();
//                    application.getPreferences().setSearchLogId(searchLogId);
//                    //application.getPreferences().setSearchLogId(((Map<String, Object>) apiResponse.getData()).get("searchLog_id").toString());
//                    onRequestComplete.onSuccess(DataMapParser.parsePlaces(dataMap));
//                } else {
//                    onRequestComplete.onFailure(apiResponse);
//                }
//            }
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                onRequestComplete.onFailure(new ApiResponse().setError(ApiResponse.ApiError.COMMUNICATION_ERROR));
//            }
//        });
//    }
//
//    public void  getPlaceSearchResult(String searchTerm, final onRequestComplete onRequestComplete) {
//        lumsticService.getPlaceSearchResults(searchTerm, new Callback<ApiResponse>() {
//            @Override
//            public void success(ApiResponse apiResponse, Response response) {
//                if (apiResponse.isSuccess()) {
//                    Map<String, Object> dataMap = (Map<String, Object>) apiResponse.getData();
//                    onRequestComplete.onSuccess(DataMapParser.parsePlaces(dataMap));
//                } else {
//                    onRequestComplete.onFailure(apiResponse);
//                }
//            }
//            @Override
//            public void failure(RetrofitError retrofitError) {
//                onRequestComplete.onFailure(new ApiResponse().setError(ApiResponse.ApiError.COMMUNICATION_ERROR));
//            }
//        });
//    }

    /**
     * End api requests
     */

    /**
     * REST Adapter Configuration
     */
    private void createRestAdapter() {
        ObjectMapper objectMapper = new ObjectMapper();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new JacksonConverter(objectMapper))
                .setErrorHandler(new ErrorHandler() {
                    @Override
                    public Throwable handleError(RetrofitError error) {
                        application.getLogger();
                        final Response response = error.getResponse();
                        if (response != null) {
                            int statusCode = response.getStatus();
                            if (error.isNetworkError() || (500 <= statusCode && statusCode < 600)) {
                                return new HttpRetryException(Logger.TAG, statusCode);
                            }
                        }
                        return error;
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog(Logger.TAG))
                //.setEndpoint(application.getResources().getString(R.string.server_url))
                .setRequestInterceptor(getRequestInterceptor())
                .build();

        lumsticService = restAdapter.create(LumsticService.class);
    }

    private RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                //Add Headers here
                request.addHeader("Accept", "application/json");
                //request.addHeader("api_key", application.getString(R.string.api_key));

                if (application.getPreferences().addAuthInHeader()) {
                    String authToken = application.getPreferences().getAuthToken();
                    request.addHeader("auth_token", authToken);
                }
            }
        };
    }

    /**
     * End REST Adapter Configuration
     */

    public void setLumsticService(LumsticService lumsticService) {
        this.lumsticService = lumsticService;
    }

    public LumsticApp getApplication() {
        return application;
    }

    public void setApplication(LumsticApp application) {
        this.application = application;
    }

}