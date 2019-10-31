package com.haroldadmin.moonshot.missions

import com.airbnb.epoxy.TypedEpoxyController
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.vector.fragmentViewModel

class MissionFragment : ComplexMoonShotFragment<MissionViewModel, MissionState>() {

    override val viewModel: MissionViewModel by fragmentViewModel()

    override fun renderer(state: MissionState) {
    }

    override fun epoxyController(): TypedEpoxyController<MissionState> = asyncController(viewModel) {
    }

}