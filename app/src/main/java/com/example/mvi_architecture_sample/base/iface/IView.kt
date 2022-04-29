package com.example.mvi_architecture_sample.base.iface

interface IView<S: IState, SE: ISideEffect> {
    fun render(state: S)
    fun navigate(from: String)
}