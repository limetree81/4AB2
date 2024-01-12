package com.example.a4ab2

import Task
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a4ab2.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle("Today")

        val ymdFormat: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val today = ymdFormat.format(Calendar.getInstance().time)

        val db = DBHelper.getInstance(this).writableDatabase
        val tasks = mutableListOf<Task>()

        var byDate = Task.getByDate(db,today)
        var fromPlan = Task.getFromPlan(db,today)
        tasks.addAll(mergeTasks(byDate,fromPlan))

        binding.todayWorks.layoutManager = LinearLayoutManager(this)
        binding.todayWorks.adapter = TaskAdapter(tasks)
        binding.todayWorks.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */)
        val usePassword = sharedPreferences.getBoolean("use_password", false)
        if(usePassword) {
            val signinIntent: Intent = Intent(this, SigninActivity::class.java)
            startActivity(signinIntent)
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.action_addPlan->{
            val intent:Intent = Intent(this,PlanAdderActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_viewPlanList->{
            val intent:Intent = Intent(this,PlanManagerActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_settings->{
            val intent:Intent = Intent(this,SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun mergeTasks(bydate: List<Task>, fromplan: List<Task>): List<Task> {
        val mergedMap = mutableMapOf<Long, Task>()

        // Step 1: Add tasks from 'bydate' to the map
        bydate.forEach { task ->
            mergedMap[task.plan.planId] = task
        }

        // Step 2: Add tasks from 'fromplan' to the map if planId is not already present
        fromplan.forEach { task ->
            if (!mergedMap.containsKey(task.plan.planId)) {
                mergedMap[task.plan.planId] = task
            }
        }

        // Step 3: Extract values from the map to get the merged list
        val mergedList = mergedMap.values.toList()

        return mergedList
    }
}