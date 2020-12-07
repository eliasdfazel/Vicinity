/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 4:25 AM
 * Last modified 12/7/20 4:24 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.geeksempire.vicinity.android.R

class ListOfPeople : Fragment() {

    companion object {

        fun openListOfPeople(activity: AppCompatActivity, vicinityDatabasePath: String) {

            val listOfPeople: ListOfPeople by lazy {
                ListOfPeople().apply {

                }
            }

            listOfPeople.arguments = Bundle().apply {
                putString("VsicinityDatabasePath", vicinityDatabasePath)
            }

            activity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right, 0)
                .replace(R.id.peopleListContainer, listOfPeople, "List Of Citizens")
                .commit()

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val vmBuilder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(vmBuilder.build())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.people_list_view, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()



    }

}