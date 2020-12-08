/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/8/20 11:28 AM
 * Last modified 12/8/20 11:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.DataProcess.PeopleDataProcess
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.Adapter.PeopleListAdapter
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.Extensions.peopleSetupUserInterface
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.databinding.PeopleListViewBinding

class ListOfPeople : Fragment() {

    companion object {

        fun openListOfPeople(activity: AppCompatActivity, vicinityDatabasePath: String) {

            val listOfPeople: ListOfPeople by lazy {
                ListOfPeople().apply {

                }
            }

            listOfPeople.arguments = Bundle().apply {
                putString("VicinityDatabasePath", vicinityDatabasePath)
            }

            activity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_from_right, 0)
                .replace(R.id.peopleListContainer, listOfPeople, "List Of Citizens")
                .commit()

        }

    }

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore


    private var peopleFirestoreCollectionPath: String? = null

    private val peopleDataProcessLiveData: PeopleDataProcess by lazy {
        ViewModelProvider(this@ListOfPeople).get(PeopleDataProcess::class.java)
    }


    lateinit var peopleListViewBinding: PeopleListViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        peopleFirestoreCollectionPath = arguments?.getString("VicinityDatabasePath")

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val vmBuilder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(vmBuilder.build())
    }

    override fun onCreateView(layoutInflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        peopleListViewBinding = PeopleListViewBinding.inflate(layoutInflater)

        return peopleListViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peopleSetupUserInterface()

        peopleListViewBinding.recyclerViewPeople.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        peopleFirestoreCollectionPath?.let { collectionPath ->

            peopleDataProcessLiveData.userInformationProfiles.observe(viewLifecycleOwner, Observer { peopleData ->

                if (peopleData.isNotEmpty()) {

                    val peopleListAdapter = PeopleListAdapter(requireContext())

                    peopleListAdapter.peopleArrayList.clear()
                    peopleListAdapter.peopleArrayList.addAll(peopleData)


                    peopleListViewBinding.recyclerViewPeople.adapter = peopleListAdapter

                } else {

                }

            })

            firestoreDatabase
                .collection(collectionPath)
                .get().addOnSuccessListener { querySnapshot ->
                    Log.d(this@ListOfPeople.javaClass.simpleName, querySnapshot.documents.size.toString())

                    if (!querySnapshot.isEmpty) {

                        peopleDataProcessLiveData.preparePeopleData(querySnapshot.documents)

                    }

                }.addOnFailureListener { exception ->
                    exception.printStackTrace()

                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()



    }

}