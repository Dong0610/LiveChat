package dong.duan.livechat.utility

import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

//@Module
//@InstallIn(ViewModelComponent::class)
//class HiltModule {
//    @Provides
//    fun providerAuthencatio(): FirebaseAuth= Firebase.auth
//
//    @Provides
//    fun provideFireStore():FirebaseFirestore= Firebase.firestore
//
//}