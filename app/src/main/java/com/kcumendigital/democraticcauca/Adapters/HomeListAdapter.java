package com.kcumendigital.democraticcauca.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcumendigital.democraticcauca.Models.Discussion;
import com.kcumendigital.democraticcauca.R;
import com.kcumendigital.democraticcauca.Util.ColletionsStatics;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

/**
 * Created by asus on 06/10/2015.
 */
public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    static final int VIEW_SPAN = 0;
    static final int VIEW_NOSPAN =1;
    static final int VIEW_PAGER =3;

    boolean pagerEnabled;


    public interface OnItemClickLister{
        void onItemclick(int position);
    }

    PagerAdpater pagerAdapter;
    Context context;
    OnItemClickLister onItemClickLister;
    FragmentManager fm;
    RecyclerView recyclerView;
     ArrayList<Discussion> data;
    Transformation transformation;

    int sizeAvatar;

    public HomeListAdapter(OnItemClickLister onItemClickLister,Context context, FragmentManager fm, RecyclerView recyclerView) {

        this.onItemClickLister = onItemClickLister;
        this.context = context;
        this.fm = fm;
        this.recyclerView = recyclerView;
        this.data = ColletionsStatics.getDataDiscusion();

        transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();

        sizeAvatar = context.getResources().getDimensionPixelSize(R.dimen.list_avatar);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_SPAN){
            View v = View.inflate(context, R.layout.template_span, null);
            v.setOnClickListener(this);

            viewHolder = new HomeListspanViewHolder(v);

        }

        else if (viewType == VIEW_NOSPAN){
            View v = View.inflate(context, R.layout.template_no_span, null);
            v.setOnClickListener(this);

            viewHolder = new HomeListNoSpanViewHolder(v);
        }

        else if (viewType == VIEW_PAGER){
            View v = View.inflate(context,R.layout.template_pager_holder,null);

            viewHolder = new HomeListPagerHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

       if (holder instanceof HomeListspanViewHolder){
           HomeListspanViewHolder spanHolder = (HomeListspanViewHolder) holder;
           if(ColletionsStatics.getHomeDiscusion().size()>0 && pagerEnabled)
               position = position - 1;
           spanHolder.title_forum_list.setText(data.get(position).getTitle());
           spanHolder.user_name.setText(data.get(position).getUser().getName());
           spanHolder.count_cometns_forums.setText("" + data.get(position).getComments());
           spanHolder.likes.setText("" + data.get(position).getLikes());
           spanHolder.dislikes.setText("" + data.get(position).getDislikes());
           spanHolder.categoria.setText(data.get(position).getCategory());
           String categoria = data.get(position).getCategory();
           Picasso.with(context).load(Uri.parse(data.get(position).getUser().getImg()))
                   .resize(sizeAvatar, sizeAvatar)
                   .centerCrop()
                   .transform(transformation).into(spanHolder.img);

           if (categoria.equals(context.getString(R.string.c_gobierno))) {
               spanHolder.leftIcon.setImageResource(R.drawable.ic_account_balance_24dp);
               //Picasso.with(context).load(R.drawable.ic_account_balance_24dp).into(spanHolder.leftIcon);
               spanHolder.leftColor.setBackgroundResource(R.color.gobierno);
               spanHolder.categoria.setTextColor(context.getResources().getColor(R.color.gobierno));
           }
           else if (categoria.equals(context.getString(R.string.c_salud))) {
               spanHolder.leftIcon.setImageResource(R.drawable.ic_local_hospital_24dp);
               //Picasso.with(context).load(R.drawable.ic_local_hospital_24dp).into(spanHolder.leftIcon);
               spanHolder.leftColor.setBackgroundResource(R.color.salud);
               spanHolder.categoria.setTextColor(context.getResources().getColor(R.color.salud));

           }
           else if (categoria.equals(context.getString(R.string.c_educacion))) {
               spanHolder.leftIcon.setImageResource(R.drawable.ic_school_24dp);
               //Picasso.with(context).load(R.drawable.ic_school_24dp).into(spanHolder.leftIcon);
               spanHolder.leftColor.setBackgroundResource(R.color.educacion);
               spanHolder.categoria.setTextColor(context.getResources().getColor(R.color.educacion));

           }else if (categoria.equals(context.getString(R.string.c_ambiente))) {
               spanHolder.leftIcon.setImageResource(R.drawable.ic_nature_white_24dp);
               //Picasso.with(context).load(R.drawable.ic_nature_white_24dp).into(spanHolder.leftIcon);
               spanHolder.leftColor.setBackgroundResource(R.color.medio_ambiente);
               spanHolder.categoria.setTextColor(context.getResources().getColor(R.color.medio_ambiente));
           }

           if (data.get(position).getReport() > 100){
               ((HomeListspanViewHolder) holder).maskReport.setVisibility(View.VISIBLE);
               LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((HomeListspanViewHolder) holder).linearParent.getLayoutParams();
               lp.height = dpToPx(45);
                       ((HomeListspanViewHolder) holder).maskReport.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       v.setVisibility(View.GONE);
                       ((HomeListspanViewHolder) holder).linearParent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                   }
               });
           }

       }

        else if (holder instanceof HomeListNoSpanViewHolder){
           HomeListNoSpanViewHolder noSpanHolder = (HomeListNoSpanViewHolder) holder;
       }

        else if (holder instanceof  HomeListPagerHolder){

           HomeListPagerHolder pagerHolder = (HomeListPagerHolder) holder;
           pagerAdapter = new PagerAdpater(fm,pagerHolder.pager,pagerHolder.marksLayout,PagerAdpater.TYPE_DISCUSION);

           pagerHolder.pager.setAdapter(pagerAdapter);
        }
    }

    @Override
    public int getItemCount() {
        if(pagerEnabled && ColletionsStatics.getHomeDiscusion().size()>0)
            return data.size()+1;
        else
            return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && pagerEnabled && ColletionsStatics.getHomeDiscusion().size()>0) {
            return  VIEW_PAGER;
        }

        else {
            return VIEW_SPAN;
        }

    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        if(pagerEnabled && ColletionsStatics.getHomeSurvey().size()>0)
            onItemClickLister.onItemclick(position-1);
        else{
            onItemClickLister.onItemclick(position);
        }

    }
//region ViewHolders
    public class HomeListspanViewHolder extends RecyclerView.ViewHolder{
        TextView title_forum_list,count_cometns_forums,user_name,likes,dislikes,categoria,maskReport;
        ImageView img,leftColor,leftIcon;
        LinearLayout linearParent;

        public HomeListspanViewHolder(View itemView) {
            super(itemView);
            title_forum_list = (TextView) itemView.findViewById(R.id.title_forum_list);
            count_cometns_forums = (TextView) itemView.findViewById(R.id.count_coments_forums);
            user_name = (TextView) itemView.findViewById(R.id.user_name_forums);
            likes = (TextView) itemView.findViewById(R.id.count_likes);
            dislikes = (TextView) itemView.findViewById(R.id.count_dislikes);
            categoria = (TextView) itemView.findViewById(R.id.categorias_list);
            img =   (ImageView) itemView.findViewById(R.id.user_img);
            leftColor = (ImageView) itemView.findViewById(R.id.left_color_category);
            leftIcon = (ImageView) itemView.findViewById(R.id.left_image_categori);
            maskReport = (TextView) itemView.findViewById(R.id.mask_report);
            linearParent = (LinearLayout) itemView.findViewById(R.id.parent_linear);
        }
    }

    public class HomeListNoSpanViewHolder extends RecyclerView.ViewHolder{
        TextView title,porcentage,opcion;
        public HomeListNoSpanViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_forum_list);
            porcentage  = (TextView) itemView.findViewById(R.id.percentage_value);
            opcion = (TextView) itemView.findViewById(R.id.Opcion);
        }


    }

    public class  HomeListPagerHolder extends RecyclerView.ViewHolder {

        ViewPager pager;
        LinearLayout marksLayout;

        public HomeListPagerHolder(View itemView) {
            super(itemView);

            pager = (ViewPager) itemView.findViewById(R.id.pager);
            marksLayout = (LinearLayout) itemView.findViewById(R.id.marks);
        }
    }

    //endregion

    public void setPagerEnabled(boolean pagerEnabled) {
        this.pagerEnabled = pagerEnabled;
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
