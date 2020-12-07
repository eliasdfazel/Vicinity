/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 6:02 AM
 * Last modified 12/7/20 6:02 AM
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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


    lateinit var peopleListViewBinding: PeopleListViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        peopleFirestoreCollectionPath = arguments?.getString("VicinityDatabasePath") ?: "https://media.giphy.com/media/ZCemAxolHlLetaTqLh/giphy.gif"

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

        peopleFirestoreCollectionPath?.let {

            firestoreDatabase
                .collection(it)
                .get().addOnSuccessListener { querySnapshot ->

                    if (!querySnapshot.isEmpty) {



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