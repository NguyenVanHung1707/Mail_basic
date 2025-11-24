package com.example.list_mail

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var adapter: EmailAdapter
    private val REQUEST_DETAIL = 1001
    private val emails = mutableListOf<Email>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        seedData()
        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerEmails)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = EmailAdapter(emails) { email, position ->
            val intent = android.content.Intent(this, EmailDetailActivity::class.java)
            intent.putExtra("email_position", position)
            intent.putExtra("email_data", email)
            startActivityForResult(intent, REQUEST_DETAIL)
        }
        recycler.adapter = adapter

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inbox -> {
                    toolbar.title = "Inbox"
                    adapter.showInbox()
                }
                R.id.nav_favorites -> {
                    toolbar.title = "Yêu thích"
                    adapter.showFavorites()
                }
                R.id.nav_recent -> {
                    toolbar.title = "Gần đây"
                    adapter.showRecent()
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabCompose).setOnClickListener {
            // Chỗ này có thể mở màn hình soạn mail mới
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_DETAIL && resultCode == RESULT_OK && data != null) {
            val pos = data.getIntExtra("email_position", -1)
            val starred = data.getBooleanExtra("email_starred", false)
            if (pos in emails.indices) {
                emails[pos].starred = starred
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Tìm mail..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText)
                return true
            }
        })
        return true
    }

    private fun seedData() {
        emails.clear()
        emails.addAll(
            listOf(
                Email("Edurila.com", "$19 Only (First 10 spots) - Bestselling...", "Are you looking to Learn Web Designing...", "12:34"),
                Email("Chris Abad", "Help make Campaign Monitor better", "Let us know your thoughts! No Images...", "11:22"),
                Email("Tuto.com", "8h de formation gratuite", "Photoshop, SEO, Blender, CSS, WordPress...", "11:04"),
                Email("support", "Société Ovh : suivi de vos services", "SAS OVH - http://www.ovh.com...", "10:26"),
                Email("Matt from Ionic", "The New Ionic Creator Is Here!", "Announcing the all-new Creator, build...", "09:55"),
                Email("Random Sender", "Weekly Update", "Here is your weekly summary...", "09:40"),
                Email("Design Team", "Sprint Review", "Please review attached slides...", "09:15")
            )
        )
    }
}