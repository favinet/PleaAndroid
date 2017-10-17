package shop.plea.and.ui.adapter.multiAdapter;

import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import shop.plea.and.ui.adapter.viewholder.BaseViewHolder;

/**
 * Created by kwon7575 on 2017-10-13.
 */

public abstract class MultiItemAdapter extends RecyclerView.Adapter<BaseViewHolder>{

    private List<Row<?>> mRows = new ArrayList<>();

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindView(getItem(position));
    }

    public <ITEM> ITEM getItem(int position)
    {
        return (ITEM) mRows.get(position).getItem();
    }

    public void addRow(Row<?> row)
    {
        this.mRows.add(row);
    }

    public void addRows(List<Row<?>> rows)
    {
        this.mRows.addAll(rows);
    }

    public void removeFromIndex(int position)
    {
        List<Row<?>> target = mRows.subList(position, mRows.size());
        target.clear();
    }

    public void setRow(int position, Row<?> row)
    {
        this.mRows.set(position, row);
    }

    public void setRows(List<Row<?>> mRows)
    {
        clear();
        this.mRows.addAll(mRows);
    }

    public void clear()
    {
        this.mRows.clear();
    }

    public static class Row<ITEM> {
        private ITEM item;
        private int itemViewType;

        private Row(ITEM item, int itemViewType)
        {
            this.item = item;
            this.itemViewType = itemViewType;
        }

        public static <T> Row<T> create(T item, int itemViewType)
        {
            return new Row<>(item, itemViewType);
        }

        public ITEM getItem() {return item;}

        public int getItemViewType() {return itemViewType;}
    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }
}
