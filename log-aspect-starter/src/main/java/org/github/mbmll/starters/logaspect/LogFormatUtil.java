package org.github.mbmll.starters.logaspect;

import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.springframework.util.StringUtils;

/**
 * LogFormatUtil.
 */
public final class LogFormatUtil {

    private LogFormatUtil() {
    }

    /**
     * getInfo.
     *
     * @param joinPoint
     * @param argsFlag
     * @return RequestInfo
     */
    public static Object[] getInfo(JoinPoint joinPoint, boolean argsFlag) {

        String methodName =
            joinPoint.getSignature().getDeclaringType().getName()
                + "."
                + joinPoint.getSignature().getName();
        if (argsFlag) {
            Object[] objs = joinPoint.getArgs();
            List<Object> args = Arrays.asList(objs);
//            args =
//                args.stream()
//                    .parallel()
//                    .map(
//                        i -> {
//                            try {
//                                return GsonUtil.t2Json(i);
//                            } catch (Exception e) {
//                                return i;
//                            }
//                        })
//                    .collect(Collectors.toList());

            return new Object[] {methodName, args};
        } else {
            return new Object[] {methodName};
        }
    }

    /**
     * メールアドレス処理.
     *
     * @param email メールアドレス
     * @return メールアドレス
     */
    public static String maskEmailAddress(String email) {
        return StringUtils.isEmpty(email) ? "" : email.replaceAll("(?<=.{1}).(?=.*@)", "*");
    }
}
