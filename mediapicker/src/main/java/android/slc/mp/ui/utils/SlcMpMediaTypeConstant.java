package android.slc.mp.ui.utils;

import android.slc.mp.R;
import android.text.TextUtils;
import android.util.Log;

public class SlcMpMediaTypeConstant {
    //audio
    public static final String EXTENSION_M3U = "m3u";
    public static final String EXTENSION_M4A = "m4a";
    public static final String EXTENSION_M4B = "m4b";
    public static final String EXTENSION_M4P = "m4p";
    public static final String EXTENSION_MP2 = "mp2";
    public static final String EXTENSION_MP3 = "mp3";
    public static final String EXTENSION_MPGA = "mpga";
    public static final String EXTENSION_OGG = "ogg";
    //video
    public static final String EXTENSION_ASF = "asf";
    public static final String EXTENSION_AVI = "avi";
    public static final String EXTENSION_M4U = "m4u";
    public static final String EXTENSION_M4V = "m4v";
    public static final String EXTENSION_MOV = "mov";
    public static final String EXTENSION_MPE = "mpe";
    public static final String EXTENSION_MPEG = "mpeg";
    public static final String EXTENSION_MPG = "mpg";
    public static final String EXTENSION_MPG4 = "mpg4";
    public static final String EXTENSION_MP4 = "mp4";
    //office
    public static final String EXTENSION_DOC = "doc";
    public static final String EXTENSION_DOCX = "docx";
    public static final String EXTENSION_XLS = "xls";
    public static final String EXTENSION_XLSX = "xlsx";
    public static final String EXTENSION_PPT = "ppt";
    public static final String EXTENSION_PPTX = "pptx";
    //pdf
    public static final String EXTENSION_PDF = "pdf";
    //img
    public static final String EXTENSION_BMP = "bmp";
    public static final String EXTENSION_GIF = "gif";
    public static final String EXTENSION_IMAGE = "image";
    public static final String EXTENSION_JPEG = "jpeg";
    public static final String EXTENSION_JPG = "jpg";
    public static final String EXTENSION_PNG = "png";
    //超文本格式
    public static final String EXTENSION_HTM = "htm";
    public static final String EXTENSION_HTML = "html";
    public static final String EXTENSION_XML = "xml";
    public static final String EXTENSION_IML = "iml";
    //压缩包
    public static final String EXTENSION_GTAR = "gtar";
    public static final String EXTENSION_GZ = "gz";
    public static final String EXTENSION_JAR = "jar";
    public static final String EXTENSION_ZIP = "zip";
    public static final String EXTENSION_Z = "z";
    //apk
    public static final String EXTENSION_APK = "apk";
    //txt
    public static final String EXTENSION_TXT = "txt";
    public static final String EXTENSION_JS = "js";
    public static final String EXTENSION_KT = "kt";
    public static final String EXTENSION_INI = "ini";
    public static final String EXTENSION_BAT = "bat";
    public static final String EXTENSION_SH = "sh";
    public static final String EXTENSION_KEY = "key";
    public static final String EXTENSION_JAVA = "java";
    public static final String EXTENSION_SCRIPT = "script";
    public static final String EXTENSION_PROPERTIES = "properties";

    public static int getIconByMediaType(String mediaType) {
        Log.i("MediaType", mediaType+"");
        if(TextUtils.isEmpty(mediaType)){
            return R.drawable.slc_mp_ic_unknown;
        }
        switch (mediaType) {
            case EXTENSION_M3U:
            case EXTENSION_M4A:
            case EXTENSION_M4B:
            case EXTENSION_M4P:
            case EXTENSION_MP2:
            case EXTENSION_MP3:
            case EXTENSION_MPGA:
            case EXTENSION_OGG:
                return R.drawable.slc_mp_ic_audiotrack;
            case EXTENSION_ASF:
            case EXTENSION_AVI:
            case EXTENSION_M4U:
            case EXTENSION_M4V:
            case EXTENSION_MOV:
            case EXTENSION_MPE:
            case EXTENSION_MPEG:
            case EXTENSION_MPG:
            case EXTENSION_MPG4:
            case EXTENSION_MP4:
                return R.drawable.slc_mp_ic_videocam;
            case EXTENSION_DOC:
            case EXTENSION_DOCX:
                return R.drawable.slc_mp_ic_word;
            case EXTENSION_XLS:
            case EXTENSION_XLSX:
                return R.drawable.slc_mp_ic_excel;
            case EXTENSION_PPT:
            case EXTENSION_PPTX:
                return R.drawable.slc_mp_ic_powerpoint;
            case EXTENSION_PDF:
                return R.drawable.slc_mp_ic_pdf;
            case EXTENSION_BMP:
            case EXTENSION_GIF:
            case EXTENSION_IMAGE:
            case EXTENSION_JPEG:
            case EXTENSION_JPG:
            case EXTENSION_PNG:
                return R.drawable.slc_mp_ic_image;
            case EXTENSION_HTM:
            case EXTENSION_HTML:
            case EXTENSION_XML:
            case EXTENSION_IML:
                return R.drawable.slc_mp_ic_html;
            case EXTENSION_APK:
                return R.drawable.slc_mp_ic_android;
            case EXTENSION_GTAR:
            case EXTENSION_GZ:
            case EXTENSION_JAR:
            case EXTENSION_ZIP:
            case EXTENSION_Z:
                return R.drawable.slc_mp_ic_cs;
            case EXTENSION_TXT:
            case EXTENSION_JS:
            case EXTENSION_KT:
            case EXTENSION_INI:
            case EXTENSION_BAT:
            case EXTENSION_SH:
            case EXTENSION_KEY:
            case EXTENSION_JAVA:
            case EXTENSION_SCRIPT:
            case EXTENSION_PROPERTIES:
                return R.drawable.slc_mp_ic_text;
            default:
                return R.drawable.slc_mp_ic_unknown;

        }
    }
}
