package com.kcumendigital.democratic.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kcumendigital.democratic.Models.Discussion;
import com.kcumendigital.democratic.R;
import com.kcumendigital.democratic.Util.ColletionsStatics;
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

    public HomeListAdapter(OnItemClickLister onItemClickLister,Context context, FragmentManager fm, RecyclerView recyclerView) {

        this.onItemClickLister = onItemClickLister;
        this.context = context;
        this.fm = fm;
        this.recyclerView = recyclerView;
        this.data = ColletionsStatics.getDataDiscusion();

        transformation = new RoundedTransformationBuilder()
                .oval(true)
                .build();
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


       if (holder instanceof HomeListspanViewHolder){
           HomeListspanViewHolder spanHolder = (HomeListspanViewHolder) holder;
          if (position < data.size()-2) {
              position = position + 2;
              spanHolder.title_forum_list.setText(data.get(position).getTitle());
              spanHolder.user_name.setText(data.get(position).getUser().getUserName());
              spanHolder.count_cometns_forums.setText("" + data.get(position).getComments());
              spanHolder.likes.setText("" + data.get(position).getLikes());
              spanHolder.likes.setText("" + data.get(position).getDislikes());
              spanHolder.categoria.setText(data.get(position).getCategory());
              String categoria = data.get(position).getCategory();
              Picasso.with(context).load(Uri.parse(data.get(position).getUser().getImg()))
                      .transform(transformation).into(spanHolder.img);

              if (categoria.equals("Gobierno")) {
                  Picasso.with(context).load(R.drawable.ic_account_balance_white_18dp).into(spanHolder.leftIcon);
                  spanHolder.leftColor.setBackgroundResource(R.color.gobierno);

              }
              if (categoria.equals("Salud")) {
                  spanHolder.leftIcon.setBackgroundResource(R.drawable.ic_local_hospital_white_18dp);
                  spanHolder.leftColor.setBackgroundResource(R.color.salud);

              }
              if (categoria.equals("Educación")) {
                  spanHolder.leftIcon.setBackgroundResource(R.drawable.ic_school_white_18dp);
                  spanHolder.leftColor.setBackgroundResource(R.color.educacion);

              }
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
        return data.size()-2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return  VIEW_PAGER;

        }

        else {
            return VIEW_SPAN;
        }

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context,"Click en un item de la lista",Toast.LENGTH_LONG).show();
        int position = recyclerView.getChildAdapterPosition(v);
        onItemClickLister.onItemclick(position+2);

    }
//region ViewHolders
    public class HomeListspanViewHolder extends RecyclerView.ViewHolder{
        TextView title_forum_list,count_cometns_forums,user_name,likes,dislikes,categoria;
        ImageView img,leftColor,leftIcon;

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
}
