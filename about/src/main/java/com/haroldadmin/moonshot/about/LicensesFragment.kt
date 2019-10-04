package com.haroldadmin.moonshot.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.haroldadmin.moonshot.about.views.licenseView
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController

class LicensesFragment: MoonShotFragment() {

    private lateinit var rvLicenses: EpoxyRecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_licenses, container, false)
        rvLicenses = root.findViewById(R.id.rvLicenses)
        rvLicenses.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvLicenses.setControllerAndBuildModels(epoxyController)
    }

    private val epoxyController: EpoxyController = simpleController {
        licenses.map { licenseEntry ->
            licenseView {
                id(licenseEntry.key)
                license(licenseEntry.key)
                onClick { _ ->
                    Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(licenseEntry.value)
                    }.also {
                        startActivity(it)
                    }
                }
            }
        }
    }
}