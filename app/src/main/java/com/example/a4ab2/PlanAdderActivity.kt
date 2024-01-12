package com.example.a4ab2

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import com.example.a4ab2.databinding.ActivityPlanAdderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class PlanAdderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlanAdderBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.planaddtoolbar)
        supportActionBar?.setTitle("일정 추가")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val today = Calendar.getInstance()
        var firstdate: String = SimpleDateFormat("yyyyMMdd").format(today.time)
        binding.firstdatepickbutton.setText(SimpleDateFormat("yyyy - MM - dd").format(today.time))
        binding.firstdatepickbutton.setOnClickListener {
            DatePickerDialog(this,object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    binding.firstdatepickbutton.setText("$p1 - ${p2+1} - $p3")
                    firstdate = p1.toString() + (p2+1).toString() + p3.toString()

                }
            },2024,0,1).show()
        }


        binding.chk0.setOnClickListener{ binding.chk0.isChecked = !binding.chk0.isChecked }
        binding.chk1.setOnClickListener{ binding.chk1.isChecked = !binding.chk1.isChecked }
        binding.chk2.setOnClickListener{ binding.chk2.isChecked = !binding.chk2.isChecked }
        binding.chk3.setOnClickListener{ binding.chk3.isChecked = !binding.chk3.isChecked }
        binding.chk4.setOnClickListener{ binding.chk4.isChecked = !binding.chk4.isChecked }
        binding.chk5.setOnClickListener{ binding.chk5.isChecked = !binding.chk5.isChecked }
        binding.chk6.setOnClickListener{ binding.chk6.isChecked = !binding.chk6.isChecked }



        binding.submitbutton.setOnClickListener {
            val db = DBHelper.getInstance(this).writableDatabase
            Plan().apply{
                this.name = binding.editname.text.toString()
                this.firstDate = firstdate
                this.repeatSunday = binding.chk0.isChecked
                this.repeatMonday = binding.chk1.isChecked
                this.repeatTuesday = binding.chk2.isChecked
                this.repeatWednesday = binding.chk3.isChecked
                this.repeatThursday = binding.chk4.isChecked
                this.repeatFriday = binding.chk5.isChecked
                this.repeatSaturday = binding.chk6.isChecked
            }.insert(db)
            db.close()
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}