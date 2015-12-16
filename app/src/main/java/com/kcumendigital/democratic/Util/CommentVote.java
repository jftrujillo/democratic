package com.kcumendigital.democratic.Util;

import com.kcumendigital.democratic.Models.Comment;
import com.kcumendigital.democratic.Models.CommentScore;
import com.kcumendigital.democratic.Models.DiscussionScore;
import com.kcumendigital.democratic.parse.SunshineParse;
import com.kcumendigital.democratic.parse.SunshineQuery;
import com.kcumendigital.democratic.parse.SunshineRecord;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by Dario Chamorro on 16/12/2015.
 */
public class CommentVote implements SunshineParse.SunshineCallback {

    public static final int REQUEST_COMMENT_SCORE_LIKE = 0;
    public static final int REQUEST_COMMENT_SCORE_DISLIKE = 1;

    public interface OnCommentVote{
        void onCommentVote();
    }

    String commentId;
    String userId;
    SunshineParse parse;
    int pos;

    OnCommentVote commentVote;

    public CommentVote(String commentId, String userId,int pos, int request, SunshineParse parse, OnCommentVote commentVote) {
        this.commentId = commentId;
        this.userId = userId;
        this.parse = parse;
        this.pos = pos;
        this.commentVote = commentVote;

        SunshineQuery queryVotes = new SunshineQuery();
        queryVotes.addUser("user", userId);
        queryVotes.addPointerValue("comment",commentId);
        parse.getAllRecords(queryVotes, this, request, CommentScore.class);
    }


    @Override
    public void done(boolean success, ParseException e) {

    }

    @Override
    public void resultRecord(boolean success, SunshineRecord record, ParseException e) {

    }

    @Override
    public void resultListRecords(boolean success, Integer requestCode, List<SunshineRecord> records, ParseException e) {
        if (requestCode == REQUEST_COMMENT_SCORE_LIKE){
            processCommentScore(records, CommentScore.LIKE);
        } else if (requestCode == REQUEST_COMMENT_SCORE_DISLIKE){
            processCommentScore(records, CommentScore.DISLIKE);
        }
    }

    private void processCommentScore(List<SunshineRecord> records, String like) {
        if (records.size() > 0) {
            CommentScore scoreC = (CommentScore) records.get(0);
            if (scoreC.getType().equals(like)) {}
            else {
                scoreC.setType(like);
                parse.update(scoreC, false, false, this);
                if (like.equals(CommentScore.LIKE)) {
                    parse.decrementField(commentId, "dislikes", Comment.class);
                    parse.incrementField(commentId, "likes", Comment.class);
                    ColletionsStatics.getDataComments().get(pos).setLikes(ColletionsStatics.getDataComments().get(pos).getLikes() + 1);
                    ColletionsStatics.getDataComments().get(pos).setDislikes(ColletionsStatics.getDataComments().get(pos).getDislikes() - 1);
                } else {
                    parse.decrementField(commentId, "likes", Comment.class);
                    parse.incrementField(commentId, "dislikes", Comment.class);

                    ColletionsStatics.getDataComments().get(pos).setLikes(ColletionsStatics.getDataComments().get(pos).getLikes() - 1);
                    ColletionsStatics.getDataComments().get(pos).setDislikes(ColletionsStatics.getDataComments().get(pos).getDislikes() + 1);
                }
            }

        } else {
            CommentScore commentScore = new CommentScore();
            commentScore.setComment(commentId);
            commentScore.setUser(userId);
            commentScore.setType(like);
            parse.insert(commentScore);
            if (like.equals(CommentScore.LIKE)) {
                parse.incrementField(commentId, "likes", Comment.class);
                ColletionsStatics.getDataComments().get(pos).setLikes(ColletionsStatics.getDataComments().get(pos).getLikes() + 1);
            } else {
                parse.incrementField(commentId, "dislikes", Comment.class);
                ColletionsStatics.getDataComments().get(pos).setDislikes(ColletionsStatics.getDataComments().get(pos).getDislikes() + 1);
            }

        }

        commentVote.onCommentVote();
    }
}
