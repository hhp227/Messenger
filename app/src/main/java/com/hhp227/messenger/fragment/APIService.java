package com.hhp227.messenger.fragment;

import com.hhp227.messenger.notification.MyResponse;
import com.hhp227.messenger.notification.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxpImdO4:APA91bHUtLZkjgPKf6fJmYLn3G-sfV3Vf1xUCD9l6IxwqsI5fTbmAKl6j8vMC9fhnp-3KEw21h62Ljx3tQyiVHqeiJR08wihnUVBJLyC6TL-wCZ13z_LB9SWd5d63mI7inm4FNUwa--i"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
