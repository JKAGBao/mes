package project.bridgetek.com.applib.main.toos;

import project.bridgetek.com.applib.main.bean.Label;
import project.bridgetek.com.applib.main.bean.Login;
import project.bridgetek.com.applib.main.bean.body.CheckItemBody;
import project.bridgetek.com.applib.main.bean.body.LabelBody;
import project.bridgetek.com.applib.main.bean.body.LoginBody;
import project.bridgetek.com.bridgelib.toos.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {
    @POST(Constants.CHECKITEM)
    Call<String> upCheckItem(@Body CheckItemBody checkItemBody);

    @POST(Constants.LABEL)
    Call<LabelBody> upLabel(@Body Label label);

    @POST(Constants.LOGIN)
    Call<Login> setLogin(@Body LoginBody loginBody);
}
