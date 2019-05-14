package com.dannie.setrise.ui.screens.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.dannie.setrise.R
import com.dannie.setrise.ui.MainActivity

abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    protected val viewModel by lazy { provideViewModel() }

    abstract fun provideViewModel(): VM

    /**
     * Don't need to override fun onCreateView every time. Just assign this property instead
     */
    protected abstract val layoutId: Int

    /**
     * Don't need to override this fun in every fragment. Just assign @layoutId instead
     * @return inflated view with @param layoutId
     */
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    /**
     * One function for all BaseFragment's where view's will be initialized.
     * Might not be in use, as kotlinx.android.synthetic approach is used in this project
     */
    protected open fun initUi(view: View) {}

    /**
     * A place for all Observable's to be subscribed to via LiveData
     */
    protected open fun subscribeToObservables() {}

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    /**
     * Override for not calling fun initUi(view) in every fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
        subscribeToObservables()
        viewModel.onViewCreated()
    }

    /**
     * Focuses on view and shows soft keyboard
     * @param view on which to focus
     */
    protected fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    protected fun showLoading() {
        activity?.let {
            (activity as MainActivity).showLoading()
        }
    }

    protected fun hideLoading() {
        activity?.let {
            (activity as MainActivity).hideLoading()
        }
    }

    protected fun replaceFragment(
        fragment: BaseFragment<*>,
        @AnimRes enterAnimId: Int,
        @AnimRes exitAnimId: Int,
        @AnimRes popEnterAnimId: Int,
        @AnimRes popExitAnimId: Int
    ) {
        replaceFragment(fragment, true, enterAnimId, exitAnimId, popEnterAnimId, popExitAnimId)
    }

    protected fun replaceFragment(
        fragment: BaseFragment<*>,
        addToBackStack: Boolean,
        @AnimRes enterAnimId: Int,
        @AnimRes exitAnimId: Int,
        @AnimRes popEnterAnimId: Int,
        @AnimRes popExitAnimId: Int
    ) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(enterAnimId, exitAnimId, popEnterAnimId, popExitAnimId)
        transaction?.replace(R.id.container, fragment)
        if (addToBackStack) {
            transaction?.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction?.commit()
    }
}