package xyz.zimuju.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextLinkUtils {


    @SuppressLint("NewApi")
    public static void textViewSpan(Context mContext, TextView textview, String content, TextClick inter) {

        SpannableString span = SpannableString.valueOf(content);
        List<PositionItem> positionList = paresString(content);
        for (int i = 0; i < positionList.size(); i++) {
            PositionItem pi = positionList.get(i);
            span.setSpan(new TextClickSapn(mContext, pi, inter), pi.start, pi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new UnderlineSpan(), pi.start, pi.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        textview.setText(span);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
    }


    public static List<PositionItem> paresString(String content) {
//		String regex = "@[^\\s:：《]+([\\s:：《]|$)|#(.+?)#|(http://|https://){1}[\\w\\.\\-/:]+|\\[(.*?)\\]";
        String regex = "(http://|https://){1}[\\w\\.\\-/:]+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        boolean b = m.find();
        List<PositionItem> list = new ArrayList<PositionItem>();
        int count = 0;
        int lastIndex = 0;
        int index = 0;
        while (b) {
            System.out.println(m.start());
            System.out.println(m.end());
            System.out.println(m.group());
            int start = m.start();
            int end = m.end();
            String str = m.group();
            if (str.startsWith("#")) {
                count++;
                if (count % 2 == 0) {
                    b = m.find(lastIndex);
                    continue;
                }
            }
            list.add(new PositionItem(start, end, str, content.length(), index));
            b = m.find(m.end() - 1);
            try {
                lastIndex = m.start() + 1;
            } catch (Exception e) {
                e.printStackTrace();
            }
            index++;
        }


        return list;
    }


    public interface TextClick {

        void onClick(String str);
    }

    private static class TextClickSapn extends ClickableSpan {
        private PositionItem item;
        private Context mContext;
        private TextClick button;

        public TextClickSapn(Context context, PositionItem item, TextClick inter) {
            this.item = item;
            this.mContext = context;
            button = inter;
        }

        @Override
        public void onClick(View widget) {
            button.onClick(item.getContent());
        }


    }

    public static class PositionItem {
        public int start;
        public int end;
        public String content;
        public int index;
        private int prefixType;
        private int strLenght;

        public PositionItem(int start, int end, String content, int strLenght, int index) {
            this.start = start;
            this.end = end;
            this.content = content;
            this.strLenght = strLenght;
            this.index = index;
        }

        public PositionItem(int start, int end, String content) {
            this.start = start;
            this.end = end;
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public String getContentWithoutPrefix() {
            switch (getPrefixType()) {
                case 1:
                    if (end == strLenght)
                        return content.substring(1, strLenght);
                    return content.substring(1, content.length() - 1);
                case 2:
                    return content.substring(1, content.length() - 1);
                case 3:
                    return content;
                default:
                    return content;
            }
        }

        /**
         * 1 @ 人物 2 # 话题 3 http://t.cn/ 短链 4 [ 表情
         *
         * @return
         */
        public int getPrefixType() {
            if (content.startsWith("@"))
                return 1;
            if (content.startsWith("#"))
                return 2;
            if (content.startsWith("http://"))
                return 3;
            if (content.startsWith("["))
                return 4;
            return -1;
        }
    }

    @SuppressLint("ParcelCreator")
    private static class NoUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

}
