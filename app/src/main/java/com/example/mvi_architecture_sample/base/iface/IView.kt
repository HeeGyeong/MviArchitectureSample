package com.example.mvi_architecture_sample.base.iface

interface IView<S: IState> {
    fun render(state: S)
}