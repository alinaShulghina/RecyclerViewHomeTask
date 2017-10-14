package com.example.alin_.animationht.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;

import android.view.animation.LinearInterpolator;

import com.example.alin_.animationht.adapters.UserAdapter.UserViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alin- on 10.10.2017.
 */
public class UserItemAnimator extends SimpleItemAnimator {

    private static TimeInterpolator sDefaultInterpolator;
    private List<UserViewHolder> mPendingRemovals = new ArrayList<>();
    private List<UserViewHolder> mPendingAdditions = new ArrayList<>();
    private List<ChangeInfo> mPendingChanges = new ArrayList<>();
    private List<MoveInfo> mPendingMoves = new ArrayList<>();

    private List<List<UserViewHolder>> mAdditionsList = new ArrayList<>();
    private List<List<MoveInfo>> mMovesList = new ArrayList<>();
    private List<List<ChangeInfo>> mChangesList = new ArrayList<>();

    private List<UserViewHolder> mAddAnimations = new ArrayList<>();
    private List<UserViewHolder> mMoveAnimations = new ArrayList<>();
    private List<UserViewHolder> mRemoveAnimations = new ArrayList<>();
    private List<UserViewHolder> mChangeAnimations = new ArrayList<>();

    private static class ChangeInfo {
        private UserViewHolder oldHolder, newHolder;
        private int fromX, fromY, toX, toY;

        private ChangeInfo(UserViewHolder oldHolder, UserViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }

        ChangeInfo(UserViewHolder oldHolder, UserViewHolder newHolder,
                   int fromX, int fromY, int toX, int toY) {
            this(oldHolder, newHolder);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }


    }

    private static class MoveInfo {
        private UserViewHolder holder;
        private int fromX, fromY, toX, toY;

        MoveInfo(UserViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    private void resetAnimation(UserViewHolder holder) {
        if (sDefaultInterpolator == null) {
            sDefaultInterpolator = new LinearInterpolator();
        }
        holder.itemView.animate().setInterpolator(sDefaultInterpolator);
        holder.message.animate().setInterpolator(sDefaultInterpolator);
        holder.userPhoto.animate().setInterpolator(sDefaultInterpolator);
        endAnimation(holder);
    }

    @Override
    public boolean animateRemove(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        resetAnimation(userViewHolder);
        mPendingRemovals.add(userViewHolder);
        return true;
    }

    private void animateRemoveView(final UserViewHolder userViewHolder) {
        mRemoveAnimations.add(userViewHolder);
        Runnable itemDisappear = new Runnable() {
            @Override
            public void run() {
                final ViewPropertyAnimator animation = userViewHolder.itemView.animate();
                animation
                        .setDuration(getRemoveDuration() / 2)
                        .scaleX(0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                dispatchRemoveStarting(userViewHolder);
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                animation.setListener(null);
                                userViewHolder.itemView.setAlpha(1f);
                                userViewHolder.itemView.setTranslationY(0);
                                userViewHolder.userPhoto.setAlpha(1f);
                                userViewHolder.itemView.setScaleX(1f);
                                userViewHolder.userPhoto.setTranslationY(0);
                                dispatchRemoveFinished(userViewHolder);
                                mRemoveAnimations.remove(userViewHolder);
                                dispatchFinished();
                            }
                        });
            }
        };

        final ViewPropertyAnimator animation = userViewHolder.userPhoto.animate();
        animation
                .setDuration(getRemoveDuration() / 2)
                .setInterpolator(new AccelerateInterpolator())
                .translationY(500)
                .withEndAction(itemDisappear)
                .start();
    }

    @Override
    public boolean animateAdd(final android.support.v7.widget.RecyclerView.ViewHolder holder) {
        UserViewHolder userViewHolder = (UserViewHolder) holder;
        resetAnimation(userViewHolder);
        userViewHolder.itemView.setTranslationY(1000);
        mPendingAdditions.add(userViewHolder);
        return true;
    }

    private void animateAddView(final UserViewHolder userViewHolder) {
        mAddAnimations.add(userViewHolder);

        final Runnable imageRotate = new Runnable() {
            @Override
            public void run() {
                final ViewPropertyAnimator animation = userViewHolder.userPhoto.animate();
                animation
                        .setDuration(getAddDuration() / 4)
                        .rotation(720)
                        .setInterpolator(new LinearInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                dispatchAddStarting(userViewHolder);
                            }

                            public void onAnimationCancel(Animator animation) {
                                userViewHolder.userPhoto.setRotation(0f);
                                userViewHolder.itemView.setTranslationY(0);
                                userViewHolder.itemView.setAlpha(1f);
                                userViewHolder.message.setTranslationY(0);
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                animation.setListener(null);
                                userViewHolder.userPhoto.setRotation(0f);
                                userViewHolder.itemView.setTranslationY(0);
                                userViewHolder.itemView.setAlpha(1f);
                                userViewHolder.message.setTranslationY(0);
                                dispatchAddFinished(userViewHolder);
                                mAddAnimations.remove(userViewHolder);
                                dispatchFinished();
                            }
                        });
            }
        };

        final Runnable messageDown = new Runnable() {
            @Override
            public void run() {
                userViewHolder.message.animate()
                        .setDuration(getAddDuration() / 4)
                        .setInterpolator(new BounceInterpolator())
                        .translationY(0)
                        .withEndAction(imageRotate);
            }
        };

        Runnable messageUp = new Runnable() {
            @Override
            public void run() {
                userViewHolder.message.animate()
                        .setDuration(getAddDuration() / 4)
                        .translationY(-40)
                        .withEndAction(messageDown);
            }
        };

        final ViewPropertyAnimator animation = userViewHolder.itemView.animate();
        animation
                .setDuration(getAddDuration() / 4)
                .setInterpolator(new AccelerateInterpolator())
                .translationY(0)
                .alpha(1f)
                .withEndAction(messageUp)
                .start();
    }

    @Override
    public boolean animateMove(android.support.v7.widget.RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        final View view = holder.itemView;
        fromX += (int) holder.itemView.getTranslationX();
        fromY += (int) holder.itemView.getTranslationY();

        resetAnimation((UserViewHolder) holder);

        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder);
            return false;
        }
        if (deltaX != 0) {
            view.setTranslationX(-deltaX);
        }
        if (deltaY != 0) {
            view.setTranslationY(-deltaY);
        }

        MoveInfo moveInfo = new MoveInfo((UserViewHolder) holder, fromX, fromY, toX, toY);
        mPendingMoves.add(moveInfo);
        return true;
    }

    private void animateMoveView(final MoveInfo moveInfo) {
        final View view = moveInfo.holder.itemView;
        final int deltaX = moveInfo.toX - moveInfo.fromX;
        final int deltaY = moveInfo.toY - moveInfo.fromY;
        if (deltaX != 0) {
            view.animate().translationX(0);
        }
        if (deltaY != 0) {
            view.animate().translationY(0);
        }
        final ViewPropertyAnimator animation = view.animate();
        mMoveAnimations.add(moveInfo.holder);
        animation.setDuration(getMoveDuration()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                dispatchMoveStarting(moveInfo.holder);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                if (deltaX != 0) {
                    view.setTranslationX(0);
                }
                if (deltaY != 0) {
                    view.setTranslationY(0);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animation.setListener(null);
                dispatchMoveFinished(moveInfo.holder);
                mMoveAnimations.remove(moveInfo.holder);
                dispatchFinished();
            }
        }).start();
    }

    @Override
    public boolean animateChange(android.support.v7.widget.RecyclerView.ViewHolder oldHolder, android.support.v7.widget.RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        ChangeInfo changeInfo = new ChangeInfo((UserViewHolder) oldHolder, (UserViewHolder) newHolder, fromLeft, fromTop, toLeft, toTop);

        if (oldHolder == newHolder) {
            return animateMove(oldHolder, fromLeft, fromTop, toLeft, toTop);
        }

        resetAnimation((UserViewHolder) oldHolder);
        oldHolder.itemView.setTranslationX(0);
        oldHolder.itemView.setTranslationY(0);

        if (newHolder != null) {
            resetAnimation((UserViewHolder) newHolder);
            newHolder.itemView.setTranslationX(0);
            newHolder.itemView.setTranslationY(0);
            newHolder.itemView.setAlpha(1f);
        }

        mPendingChanges.add(changeInfo);
        return true;
    }

    private void animateChangeView(final ChangeInfo changeInfo) {
        final UserViewHolder holder = changeInfo.oldHolder;
        final View oldItemView = holder == null ? null : holder.itemView;
        final UserViewHolder newHolder = changeInfo.newHolder;
        final View newItemView = newHolder != null ? newHolder.itemView : null;

        if (oldItemView != null) {
            Runnable itemDown = new Runnable() {
                @Override
                public void run() {
                    final ViewPropertyAnimator animation = oldItemView.animate();
                    animation
                            .setDuration(getChangeDuration() / 2)
                            .setInterpolator(new BounceInterpolator())
                            .translationY(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    dispatchChangeStarting(changeInfo.oldHolder, true);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    oldItemView.setTranslationY(0);
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    animation.setListener(null);
                                    oldItemView.setTranslationY(0);
                                    dispatchChangeFinished(changeInfo.oldHolder, true);
                                    mChangeAnimations.remove(changeInfo.oldHolder);
                                    dispatchFinished();
                                }
                            });
                }
            };
            oldItemView.animate()
                    .setDuration(getChangeDuration() / 2)
                    .translationY(-40)
                    .withEndAction(itemDown)
                    .start();
            mChangeAnimations.add(changeInfo.oldHolder);
        }

        if (newItemView != null) {
            Runnable itemDown = new Runnable() {
                @Override
                public void run() {
                    final ViewPropertyAnimator animation = newItemView.animate();
                    animation
                            .setDuration(getChangeDuration() / 2)
                            .setInterpolator(new BounceInterpolator())
                            .translationY(0)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    dispatchChangeStarting(changeInfo.newHolder, false);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    newItemView.setTranslationY(0);
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    animation.setListener(null);
                                    newItemView.setTranslationY(0);
                                    dispatchChangeFinished(changeInfo.newHolder, false);
                                    mChangeAnimations.remove(changeInfo.newHolder);
                                    dispatchFinished();
                                }
                            });
                }
            };

            mChangeAnimations.add(changeInfo.newHolder);
            final ViewPropertyAnimator animation = newItemView.animate();
            animation
                    .setDuration(getChangeDuration() / 2)
                    .translationY(-40)
                    .withEndAction(itemDown)
                    .start();
        }
    }

    @Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        boolean changesPending = !mPendingChanges.isEmpty();
        boolean movesPending = !mPendingMoves.isEmpty();

        if (!removalsPending && !additionsPending && !changesPending && !movesPending) {
            return;
        }

        for (UserViewHolder holder : mPendingRemovals) {
            animateRemoveView(holder);
        }

        mPendingRemovals.clear();

        if (movesPending) {
            final ArrayList<MoveInfo> moves = new ArrayList<>();
            moves.addAll(mPendingMoves);
            mMovesList.add(moves);
            mPendingMoves.clear();
            Runnable moveExecutor = new Runnable() {
                @Override
                public void run() {
                    boolean removed = mMovesList.remove(moves);
                    if (!removed) {
                        return;
                    }
                    for (MoveInfo moveInfo : moves) {
                        animateMoveView(moveInfo);
                    }
                    moves.clear();
                }
            };
            if (removalsPending) {
                View view = moves.get(0).holder.itemView;
                ViewCompat.postOnAnimationDelayed(view, moveExecutor, getRemoveDuration());
            } else {
                moveExecutor.run();
            }
        }

        if (changesPending) {
            final ArrayList<ChangeInfo> changes = new ArrayList<>();
            changes.addAll(mPendingChanges);
            mChangesList.add(changes);
            mPendingChanges.clear();
            Runnable changeExecutor = new Runnable() {
                @Override
                public void run() {
                    boolean removed = mChangesList.remove(changes);
                    if (!removed) {
                        return;
                    }
                    for (ChangeInfo changeInfo : changes) {
                        animateChangeView(changeInfo);
                    }
                    changes.clear();
                }
            };
            if (removalsPending || additionsPending) {
                long removeDuration = removalsPending ? getRemoveDuration() : 0;
                long additionDuration = additionsPending ? getAddDuration() / 4 : 0;
                long totalDelay = removeDuration + additionDuration;
                android.support.v7.widget.RecyclerView.ViewHolder holder = changes.get(0).oldHolder;
                ViewCompat.postOnAnimationDelayed(holder.itemView, changeExecutor, totalDelay);
            } else {
                changeExecutor.run();
            }
        }

        if (additionsPending) {
            final ArrayList<UserViewHolder> additions = new ArrayList<>();
            additions.addAll(mPendingAdditions);
            mAdditionsList.add(additions);
            mPendingAdditions.clear();
            Runnable addExecutor = new Runnable() {
                @Override
                public void run() {
                    boolean removed = mAdditionsList.remove(additions);
                    if (!removed) {
                        return;
                    }
                    for (UserViewHolder holder : additions) {
                        animateAddView(holder);
                    }
                    additions.clear();
                }
            };
            if (removalsPending || movesPending) {
                long removeDuration = removalsPending ? getRemoveDuration() : 0;
                long moveDuration = movesPending ? getMoveDuration() : 0;
                long totalDelay = removeDuration + moveDuration;
                View view = additions.get(0).itemView;
                ViewCompat.postOnAnimationDelayed(view, addExecutor, totalDelay);
            } else {
                addExecutor.run();
            }
        }
    }

    private void endChangeAnimation(List<ChangeInfo> infoList, android.support.v7.widget.RecyclerView.ViewHolder item) {
        for (int i = infoList.size() - 1; i >= 0; i--) {
            ChangeInfo changeInfo = infoList.get(i);
            if (endChangeAnimationIfNecessary(changeInfo, item)) {
                if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    infoList.remove(changeInfo);
                }
            }
        }
    }

    private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }

    private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, android.support.v7.widget.RecyclerView.ViewHolder item) {
        boolean oldItem = false;
        if (changeInfo.newHolder == item) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder == item) {
            changeInfo.oldHolder = null;
            oldItem = true;
        } else {
            return false;
        }
        item.itemView.setAlpha(1);
        item.itemView.setTranslationX(0);
        item.itemView.setTranslationY(0);
        dispatchChangeFinished(item, oldItem);
        return true;
    }

    @Override
    public void endAnimation(android.support.v7.widget.RecyclerView.ViewHolder item) {
        UserViewHolder holder = (UserViewHolder) item;

        holder.itemView.animate().cancel();
        holder.userPhoto.animate().cancel();
        holder.message.animate().cancel();

        for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
            MoveInfo moveInfo = mPendingMoves.get(i);
            if (moveInfo.holder == holder) {
                holder.itemView.setTranslationY(0);
                holder.itemView.setTranslationX(0);
                dispatchMoveFinished(holder);
                mPendingMoves.remove(i);
            }
        }

        endChangeAnimation(mPendingChanges, holder);

        if (mPendingRemovals.remove(holder)) {
            holder.userPhoto.setAlpha(0f);
            holder.userPhoto.setTranslationY(500);
            dispatchRemoveFinished(holder);
        }

        if (mPendingAdditions.remove(holder)) {
            holder.userPhoto.setRotationY(0f);
            holder.itemView.setTranslationY(0);
            holder.itemView.setAlpha(1f);
            holder.message.setTranslationY(0);
            dispatchAddFinished(holder);
        }

        for (int i = mChangesList.size() - 1; i >= 0; i--) {
            List<ChangeInfo> changes = mChangesList.get(i);
            endChangeAnimation(changes, holder);
            if (changes.isEmpty()) {
                mChangesList.remove(i);
            }
        }

        for (int i = mMovesList.size() - 1; i >= 0; i--) {
            List<MoveInfo> moves = mMovesList.get(i);
            for (int j = moves.size() - 1; j >= 0; j--) {
                MoveInfo moveInfo = moves.get(j);
                if (moveInfo.holder == holder) {
                    holder.itemView.setTranslationY(0);
                    holder.itemView.setTranslationX(0);
                    dispatchMoveFinished(holder);
                    moves.remove(j);
                    if (moves.isEmpty()) {
                        mMovesList.remove(i);
                    }
                    break;
                }
            }
        }

        for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
            List<UserViewHolder> additions = mAdditionsList.get(i);
            if (additions.remove(holder)) {
                holder.userPhoto.setRotationY(0f);
                holder.itemView.setTranslationY(0);
                holder.itemView.setAlpha(1f);
                holder.message.setTranslationY(0);
                dispatchAddFinished(holder);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(i);
                }
            }
        }

        mRemoveAnimations.remove(holder);
        mAddAnimations.remove(holder);
        mChangeAnimations.remove(holder);
        mMoveAnimations.remove(holder);

        dispatchFinished();
    }

    @Override
    public void endAnimations() {
        int count = mPendingMoves.size();
        for (int i = count - 1; i >= 0; i--) {
            MoveInfo item = mPendingMoves.get(i);
            View view = item.holder.itemView;
            view.setTranslationY(0);
            view.setTranslationX(0);
            dispatchMoveFinished(item.holder);
            mPendingMoves.remove(i);
        }

        count = mPendingRemovals.size();
        for (int i = count - 1; i >= 0; i--) {
            UserViewHolder holder = mPendingRemovals.get(i);
            holder.userPhoto.setAlpha(0f);
            holder.userPhoto.setTranslationY(500);
            dispatchRemoveFinished(holder);
            mPendingRemovals.remove(i);
        }

        count = mPendingAdditions.size();
        for (int i = count - 1; i >= 0; i--) {
            UserViewHolder holder = mPendingAdditions.get(i);
            holder.userPhoto.setRotationY(0f);
            holder.itemView.setTranslationY(0);
            holder.itemView.setAlpha(1f);
            holder.message.setTranslationY(0);
            dispatchAddFinished(holder);
            mPendingAdditions.remove(i);
        }

        count = mPendingChanges.size();
        for (int i = count - 1; i >= 0; i--) {
            endChangeAnimationIfNecessary(mPendingChanges.get(i));
        }
        mPendingChanges.clear();

        if (!isRunning()) {
            return;
        }

        int listCount = mMovesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            List<MoveInfo> moves = mMovesList.get(i);
            count = moves.size();
            for (int j = count - 1; j >= 0; j--) {
                MoveInfo moveInfo = moves.get(j);
                android.support.v7.widget.RecyclerView.ViewHolder item = moveInfo.holder;
                View view = item.itemView;
                view.setTranslationY(0);
                view.setTranslationX(0);
                dispatchMoveFinished(moveInfo.holder);
                moves.remove(j);
                if (moves.isEmpty()) {
                    mMovesList.remove(moves);
                }
            }
        }

        listCount = mAdditionsList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            List<UserViewHolder> additions = mAdditionsList.get(i);
            count = additions.size();
            for (int j = count - 1; j >= 0; j--) {
                UserViewHolder holder = additions.get(i);
                holder.userPhoto.setRotationY(0f);
                holder.itemView.setTranslationY(0);
                holder.itemView.setAlpha(1f);
                holder.message.setTranslationY(0);
                dispatchAddFinished(holder);
                additions.remove(j);
                if (additions.isEmpty()) {
                    mAdditionsList.remove(additions);
                }
            }
        }

        listCount = mChangesList.size();
        for (int i = listCount - 1; i >= 0; i--) {
            List<ChangeInfo> changes = mChangesList.get(i);
            count = changes.size();
            for (int j = count - 1; j >= 0; j--) {
                endChangeAnimationIfNecessary(changes.get(j));
                if (changes.isEmpty()) {
                    mChangesList.remove(changes);
                }
            }
        }

        cancelAll(mRemoveAnimations);
        cancelAll(mMoveAnimations);
        cancelAll(mAddAnimations);
        cancelAll(mChangeAnimations);

        dispatchAnimationsFinished();
    }

    private void cancelAll(List<UserViewHolder> userViewHolders) {
        for (int i = userViewHolders.size() - 1; i >= 0; i--) {
            userViewHolders.get(i).itemView.animate().cancel();
            userViewHolders.get(i).userPhoto.animate().cancel();
            userViewHolders.get(i).message.animate().cancel();
        }
    }

    @Override
    public boolean isRunning() {
        return (!mPendingAdditions.isEmpty()
                || !mPendingChanges.isEmpty()
                || !mPendingMoves.isEmpty()
                || !mPendingRemovals.isEmpty()
                || !mMoveAnimations.isEmpty()
                || !mRemoveAnimations.isEmpty()
                || !mAddAnimations.isEmpty()
                || !mChangeAnimations.isEmpty()
                || !mMovesList.isEmpty()
                || !mAdditionsList.isEmpty()
                || !mChangesList.isEmpty());
    }

    private void dispatchFinished() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }


}
