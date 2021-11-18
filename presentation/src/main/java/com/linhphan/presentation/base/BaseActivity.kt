package com.linhphan.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutId: Int

    protected val binding: T by lazy { getViewDataBindingInstance() }
    protected val viewModel: V by lazy { getViewModelInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        initData()
        setupViews()
        setupObservers()
    }

    protected abstract fun getBindingVariableId(): Int
    protected open fun initData() {}
    protected open fun setupViews() {}
    protected open fun setupObservers() {}

    protected open fun getViewDataBindingInstance(): T {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    protected open fun getViewModelInstance(): V {
        val typeToken = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<V>
        return ViewModelProvider(this).get(typeToken)
    }

    protected open fun setBindingVariables() {
        if (getBindingVariableId() > 0) {
            binding.setVariable(getBindingVariableId(), viewModel)
        }
    }

    private fun initDataBinding() {
        setBindingVariables()
        binding.lifecycleOwner = this@BaseActivity
    }

}
