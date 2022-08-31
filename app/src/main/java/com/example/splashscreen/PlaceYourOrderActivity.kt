package com.example.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splashscreen.adapter.PlaceYourOrderAdapter
import com.example.splashscreen.models.RestaurantModel
import kotlinx.android.synthetic.main.activity_place_your_order.*

class PlaceYourOrderActivity : AppCompatActivity() {

    var placeYourOrderAdapter: PlaceYourOrderAdapter? = null
    var isDeliveryOn: Boolean = false
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_your_order)

        val restaurantModel: RestaurantModel? = intent.getParcelableExtra("RestaurantModel")
        val actionbar: ActionBar? = supportActionBar
        actionbar?.setTitle(restaurantModel?.name)
        actionbar?.setSubtitle(restaurantModel?.address)
        actionbar?.setDefaultDisplayHomeAsUpEnabled(true)


        buttonPlaceYourOrder.setOnClickListener {
            OnPlaceOrderButtonClick(restaurantModel)

        }
        switchDelivery?.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                inputAddress.visibility = View.VISIBLE
                inputCity.visibility = View.VISIBLE
                inputZip.visibility = View.VISIBLE
                inputCounty.visibility = View.VISIBLE
                tvDeliveryCharge.visibility = View.VISIBLE
                tvDeliveryChargeAmount.visibility = View.VISIBLE
                isDeliveryOn = true
                calculateTotalAmount(restaurantModel)


            } else {
                inputAddress.visibility = View.GONE
                inputCity.visibility = View.GONE
                inputZip.visibility = View.GONE
                inputCounty.visibility = View.GONE
                tvDeliveryCharge.visibility = View.GONE
                tvDeliveryChargeAmount.visibility = View.GONE
                isDeliveryOn = false
                calculateTotalAmount(restaurantModel)
            }
        }
        initRecyclerView(restaurantModel)
        calculateTotalAmount(restaurantModel)
    }

    private fun initRecyclerView(restaurantModel: RestaurantModel?) {
        cartItemsRecyclerView.layoutManager = LinearLayoutManager(this)
        placeYourOrderAdapter = PlaceYourOrderAdapter(restaurantModel?.menus)
        cartItemsRecyclerView.adapter = placeYourOrderAdapter

    }


    private fun calculateTotalAmount(restaurantModel: RestaurantModel?) {
        var subTotalAmount = 0f
        for (menu in restaurantModel?.menus!!) {
            subTotalAmount += menu?.price!! * menu?.totalInCart!!

        }
        tvSubtotalAmount.text = "$" + String.format("%.2f", subTotalAmount)
        if (isDeliveryOn) {
            tvDeliveryChargeAmount.text =
                "$" + String.format("%.2f", restaurantModel.delivery_charge?.toFloat())
            subTotalAmount += restaurantModel?.delivery_charge?.toFloat()!!


        }
        tvTotalAmount.text = "$"+ String.format("%.2f", subTotalAmount)

    }

    private fun OnPlaceOrderButtonClick(restaurantModel: RestaurantModel?) {
        if (TextUtils.isEmpty(inputName.text.toString())) {
            inputName.error = "Enter your name"
            return
        } else if (isDeliveryOn && TextUtils.isEmpty(inputAddress.text.toString())) {
            inputAddress.error = "Enter your address"
            return

        } else if (isDeliveryOn && TextUtils.isEmpty(inputCounty.text.toString())) {
            inputCounty.error = "Enter your County/City Name"
            return
        } else if(isDeliveryOn && TextUtils.isEmpty(inputZip.text.toString())) {
            inputZip.error =  "Enter your Zip code"
            return
        } else if (TextUtils.isEmpty(inputCardNumber.text.toString())) {
            inputCardNumber.error = "Enter your Credit card Number"
            return

        } else if (TextUtils.isEmpty(inputCardExpiry.text.toString())) {
            inputCardExpiry.error = "Enter your Credit card Expiry"
            return
        } else if (TextUtils.isEmpty(inputCardPin.text.toString())) {
            inputCardPin.error = "Enter your Credit card Expiry"
            return
        }
        val intent = Intent(this@PlaceYourOrderActivity, SuccessOrderActivity::class.java)
        intent.putExtra("RestaurantModel", restaurantModel)
        startActivityForResult(intent, 1000)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 1000)
            setResult(RESULT_OK)
            finish()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            else -> {}

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_CANCELED)
        finish()
    }

}
