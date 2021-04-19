package com.headmostlab.usercontacts.data

import android.content.ContentResolver
import android.provider.ContactsContract
import com.headmostlab.usercontacts.domain.Contact
import io.reactivex.Single

class ContactRepository(private val contentResolver: ContentResolver) {
    fun getContracts(): Single<List<Contact>> = Single.create { emitter ->
        try {
            val cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            cursor?.let {
                val contacts = mutableListOf<Contact>()
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {

                        val contactId =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))

                        val phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                            null,
                            null
                        )

                        val numbers = mutableListOf<String>()

                        phoneCursor?.let {
                            while (phoneCursor.moveToNext()) {
                                numbers.add(
                                    phoneCursor.getString(
                                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                )
                            }
                        }

                        contacts.add(
                            Contact(
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                numbers
                            )
                        )
                    }
                }
                emitter.onSuccess(contacts)
            }
            cursor?.close()
        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }
}
