package xyz.zimuju.common.rx;


import io.reactivex.functions.Function;
import xyz.zimuju.common.model.ResultData;
import xyz.zimuju.common.util.EmptyUtil;
import xyz.zimuju.common.util.GsonUtil;
import xyz.zimuju.common.util.ZipUtils;

/**
 * Created by 8000m on 2017/5/2.
 */

public class RxResponse<T> implements Function<ResultData<T>, T> {

    Class<T> clazz;

    public RxResponse(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> T parseResData(ResultData<T> res, Class<T> clazz) {
        T data = null;

        if ("gzip".equals(res.getCompress())) {
            data = zipJsonToData(res.getJson(), clazz);
        } else {
            data = res.getData();
        }

        return data;
    }

    /**
     * json字符串先base64解密，再zip解压，再转换成对象T输出
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T zipJsonToData(String json, Class<T> clazz) {
        T data = null;
        if (EmptyUtil.isNotEmpty(json)) {
            long timeStart = System.currentTimeMillis();
            long size1 = json.length();
            String decompressed = ZipUtils.gunzip(json);
            if (EmptyUtil.isNotEmpty(decompressed)) {
                long size2 = decompressed.length();
                data = GsonUtil.processJson(decompressed, clazz);
                long timeEnd = System.currentTimeMillis();
                long timeDur = timeEnd - timeStart;
            }
        }
        return data;
    }

    @Override
    public T apply(ResultData<T> tResData) throws Exception {
//        Intent intent=new Intent(CommonConstants.KEY_LOGIN_LOUT);
//        CommonApplication.getInstance().getContext().sendBroadcast(intent);
        if (tResData.getCode() != 0) {
            throw new RxResponseException(tResData.getCode());
        }
        T objRes = parseResData(tResData, clazz);
        if (EmptyUtil.isEmpty(objRes)) {
            throw new RxResponseException(999);
        }
        return objRes;
    }
}
