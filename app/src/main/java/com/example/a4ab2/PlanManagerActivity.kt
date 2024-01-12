package com.example.a4ab2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4ab2.databinding.ActivityPlanManagerBinding

class PlanManagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlanManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle("Plans")

        val db = DBHelper.getInstance(this).writableDatabase
        val plans = Plan.getAll(db)

        binding.planRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.planRecyclerView.adapter = PlanAdapter(plans)
        binding.planRecyclerView.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_addPlan->{
            val intent: Intent = Intent(this,PlanAdderActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_viewPlanList->{
            val intent: Intent = Intent(this,PlanManagerActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_settings->{
            val intent: Intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}