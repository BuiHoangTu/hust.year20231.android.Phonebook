package com.bhtu.phonebook

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import java.util.Random
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private val phoneItems: MutableList<PhoneItem> = ArrayList()

    init {
        val phoneOwners = ArrayList(
            listOf(
                "John Wick",
                "Robert J",
                "James Gunn",
                "Ricky Tales",
                "Micky Mouse",
                "Pick War"
            )
        )
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        for (char in charPool) {
            phoneOwners.add("Human $char")
        }

        val random = Random()
        for (phoneOwner in phoneOwners) {
            val phoneStr = abs(random.nextInt()).toString()
            val email = "${phoneOwner.replace(' ', '.', true)}@gmail.com"
            phoneItems.add(
                PhoneItem(
                    phoneOwner,
                    PhoneNumber(phoneStr),
                    email
                )
            )
            Log.v("TAG", "New phone $phoneOwner-${PhoneNumber(phoneStr).number} added")
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item1 -> {
                Log.v("TAG", "pressed item")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val phoneItemView = findViewById<ListView>(R.id.phone_item_view)
//        recyclerView.layoutManager = LinearLayoutManager(this)
        phoneItemView.adapter = PhoneAdapter(this.phoneItems, this)

        // press and hold will trigger context menu
        registerForContextMenu(phoneItemView)

        phoneItemView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, index, _ ->
                run {
                    val selected = phoneItems[index]
                    val intent = Intent(this, PhoneItemDetailActivity::class.java)
                    intent.putExtra("id", selected.id)
                    intent.putExtra("fullName", selected.fullName)
                    intent.putExtra("phoneNumber", selected.phoneNumber.number)
                    intent.putExtra("email", selected.email)

                    startActivity(intent)
                }
            }
    }

    // #region: define menu when you click and hold
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        this.menuInflater.inflate(R.menu.phone_item_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val idx = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        val selectedEntry = this.phoneItems[idx]

        when (item.itemId) {
            R.id.item_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + selectedEntry.phoneNumber.number)
                startActivity(intent)
            }

            R.id.item_sms -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.fromParts("sms:", selectedEntry.phoneNumber.number, null)
                intent.putExtra("sms_body", "This is the message")
                startActivity(intent)
            }

            R.id.item_email -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"

                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(selectedEntry.email)) // recipients
                intent.putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                intent.putExtra(Intent.EXTRA_TEXT, "Email message text")
                startActivity(intent)
            }
        }

        return super.onContextItemSelected(item)
    }
    // #endregion

}