package com.zmx.trybase;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

// https://stackoverflow.com/questions/47987649/why-getcontext-in-fragment-sometimes-returns-null
public class BaseFragment extends Fragment {

    private Context contextNullSafe;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*View creation related to this fragment is finished here. So in case if contextNullSafe is null
         * then we can populate it here.In some discussion in - https://stackoverflow.com/questions/6215239/getactivity-returns-null-in-fragment-function
         * and https://stackoverflow.com/questions/47987649/why-getcontext-in-fragment-sometimes-returns-null,
         * there are some recommendations to call getContext() or getActivity() after onCreateView() and
         * onViewCreated() is called after the onCreateView() call every time */
        if (contextNullSafe == null) getContextNullSafety();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }

    /**
     * CALL THIS IF YOU NEED CONTEXT
     */
    public Context getContextNullSafety() {
        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }

    /**
     * CALL THIS IF YOU NEED ACTIVITY
     */
    public FragmentActivity getActivityNullSafety() {
        if (getContextNullSafety() != null && getContextNullSafety() instanceof FragmentActivity) {
            /*It is observed that if context it not null then it will be
             * the related host/container activity always*/
            return (FragmentActivity) getContextNullSafety();
        }
        return null;
    }
}
