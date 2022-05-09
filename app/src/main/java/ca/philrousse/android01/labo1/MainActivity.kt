package ca.philrousse.android01.labo1

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider


class MainActivity : AppCompatActivity() {
    private lateinit var editMenu: FlexboxLayout
    private lateinit var img: ImageView
    private lateinit var btnEdit: MenuItem
    private var editMenuIsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ////////////////////////////////////////////////
        // Get all widget that we need to manipulate
        ////////////////////////////////////////////////

        /////////
        // Top App bar Menu
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        this.btnEdit = topAppBar.menu.findItem(R.id.btn_edit)

        /////////
        // Edit Menu and all option
        this.editMenu = findViewById(R.id.edit_menu)
        val filterGroup = findViewById<ChipGroup>(R.id.filterGroup)
        val editTransparency = findViewById<Slider>(R.id.edit_transparency)
        val editBorder = findViewById<Chip>(R.id.edit_border)

        /////////
        // The image view
        this.img = findViewById(R.id.img)

        ////////////////////////////////////////////////
        // SetInitialState
        ////////////////////////////////////////////////
        this.setEditMenu(this.editMenuIsVisible)

        ////////////////////////////////////////////////
        // Set Callback
        ////////////////////////////////////////////////

        /////////
        // Top app bar

        // Set callback for Toolbar MenuItem
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId){
                R.id.btn_edit -> {
                    this.toggleEditMenu()
                    true
                }
                else -> false
            }
        }

        /////////
        // Edit Menu

        //Set callback for Filter
        filterGroup.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.filter_original -> {
                    this.img.clearColorFilter()
                }
                R.id.filter_sepia -> {
                    this.img.colorFilter = ColorMatrixColorFilter(getColorMatrixSepia())
                }
                R.id.filter_invert -> {
                    this.img.colorFilter = ColorMatrixColorFilter(getColorMatrixInverted())
                }
                R.id.filter_binary -> {
                    this.img.colorFilter = ColorMatrixColorFilter(getColorMatrixBinary())
                }
            }
        }

        //Set callback for Transparency
        editTransparency.addOnChangeListener { _, value, fromUser ->
            if(fromUser){
                this.img.alpha = value
            }
        }

        //Set callback for Border
        editBorder.setOnCheckedChangeListener { _, b ->
            if(b){
               this.img.setBackgroundResource(R.drawable.border)
            }
            else {
                this.img.background = null
            }
        }

        /////////
        // Image

        // Set callback when Clicking the Img
        this.img.setOnClickListener {
            this.toggleEditMenu()
        }
    }

    private fun setEditMenu(b: Boolean){
        this.editMenuIsVisible = b
        if(b){
            this.btnEdit.setIcon(R.drawable.ic_baseline_close_24)
            this.editMenu.visibility = View.VISIBLE
        }
        else{
            this.btnEdit.setIcon(R.drawable.ic_baseline_edit_24)
            this.editMenu.visibility = View.GONE
        }
    }
    private fun toggleEditMenu(){
        this.setEditMenu(!this.editMenuIsVisible)
    }

    private fun getColorMatrixSepia() : ColorMatrix{
        val colorMatrix = ColorMatrix()
        val colorScale = ColorMatrix()

        colorMatrix.setSaturation(0F)
        colorScale.setScale(1F,1F,0.8f,1F)

        colorMatrix.postConcat(colorScale)
        return colorMatrix
    }

    private fun getColorMatrixBinary() : ColorMatrix{
        //Value for threshold Matrix
        val m = 255F
        val t = -255 * 128F

        val threshold = ColorMatrix(floatArrayOf(
            m,0F,0F,1F,t,
            0F,m,0F,1F,t,
            0F,0F,m,1F,t,
            0F,0F,0F,1F,0F
        ))

        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0F)

        colorMatrix.postConcat(threshold)

        return colorMatrix
    }

    private fun getColorMatrixInverted(): ColorMatrix {
        //Value for Color Matrix
        val m = -1F
        val t = 255F

        return ColorMatrix(
            floatArrayOf(
                m, 0F, 0F, 0F, t,
                0F, m, 0F, 0F, t,
                0F, 0F, m, 0F, t,
                0F, 0F, 0F, 1F, 0F
            )
        )
    }

}