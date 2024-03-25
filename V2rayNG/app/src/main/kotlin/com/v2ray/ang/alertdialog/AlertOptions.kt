package com.v2ray.ang.alertdialog


import com.v2ray.ang.R
import androidx.annotation.DrawableRes


/**
 * Class which holds the options for MyAlertDialog
 */
class AlertOptions(
    val title: String,
    val text: String,
    val alternativeText: String,
    val mainText: String,
    @param:DrawableRes val icon: Int,
    val isCancelable: Boolean,
    type: AlertType
) {
    private val type: AlertType

    /**
     * Class which holds the options for MyAlertDialog
     *
     * @param title           String to show as title
     * @param text            String to show as text
     * @param alternativeText String to be displayed at alternative button
     * @param mainText        String to be displayed at main button
     * @param icon            Resource ID of the icon drawable
     * @param isCancelable    True if should be cancelable
     * @param type            The AlertType (to be returned as callback to calling activity)
     */
    init {
        this.type = type
    }

    fun updateText(newText: String): AlertOptions {
        return AlertOptions(
            title,
            newText,
            alternativeText,
            mainText,
            icon,
            isCancelable,
            type
        )
    }

    fun getType(): AlertType {
        return type
    }

    companion object {
        fun create(type: AlertType): AlertOptions {
            return when (type) {
                AlertType.Primary -> AlertOptions(
                    "Primary Alert!",
                    "This is an example of a simple primary alert. I sometimes call them also info alerts.",
                    "No, thanks",
                    "Suggested Option",
                    R.drawable.icon_primary,
                    false,
                    type
                )
                AlertType.Warning -> AlertOptions(
                    "Warning Alert!",
                    "This is an example of a warning alert. I show this to users when they have to be careful.",
                    "I don't care",
                    "Do as I should",
                    R.drawable.icon_warning,
                    false,
                    type
                )
                AlertType.Success -> AlertOptions(
                    "Success Alert!",
                    "Bravo you successfully opened a success alert! Good job. Check out the dynamic alert as well!",
                    "Back",
                    "Finish",
                    R.drawable.icon_success,
                    true,
                    type
                )
                AlertType.OneOption -> AlertOptions(
                    "Just to notify you!",
                    "This is a sigle option alert. Do you wish to proceed?",
                    "",
                    "Proceed",
                    R.drawable.icon_primary,
                    false,
                    type
                )
                AlertType.Dynamic -> AlertOptions(
                    "Dynamic Alert!",
                    "This is a dynamic alert",
                    "Option",
                    "Better Option",
                    R.drawable.icon_primary,
                    false,
                    type
                )
                else -> AlertOptions(
                    "Unknown Error",
                    "Ups, something went wrong :(",
                    "",
                    "close",
                    R.drawable.icon_warning,
                    false,
                    AlertType.Unknown
                )
            }
        }
    }
}