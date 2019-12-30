package android.slc.mp.ui.adapter;

import android.content.Context;
import android.slc.mp.R;
import android.slc.mp.ui.adapter.base.SlcMpBaseViewHolder;

import androidx.annotation.NonNull;

import android.slc.mp.po.i.IFileItem;
import android.slc.mp.ui.SlcIMpDelegate;
import android.slc.mp.ui.utils.SlcMpMediaTypeConstant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SlcMpFileAdapter extends SlcMpBaseMpAdapter<IFileItem> {
    private SimpleDateFormat fileModifiedDateFormat = new SimpleDateFormat("yy MM月 dd HH:mm:ss", Locale.getDefault());

    public SlcMpFileAdapter(@NonNull Context context, @NonNull List<IFileItem> date,
                            SlcIMpDelegate mediaPickerDelegate) {
        super(R.layout.slc_mp_item_file, context, date, mediaPickerDelegate);
    }

    @Override
    protected void convert(SlcMpBaseViewHolder helper, final IFileItem item) {
        helper.setChecked(R.id.checkbox, item.isChecked());
        helper.setText(R.id.tv_title, item.getDisplayName());
        helper.setText(R.id.tv_sub_title,
                fileModifiedDateFormat.format(new Date(item.getModified())) + "  " + formatFileSize(item.getSize()));
        helper.setImageResource(R.id.iv_icon, SlcMpMediaTypeConstant.getIconByMediaType(item.getExtension()));
    }

    /**
     * 格式化文件大小
     * @param byteNum
     * @return
     */
    protected String formatFileSize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.0fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.2fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.2fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.2fGB", (double) byteNum / 1073741824);
        }
    }
}
