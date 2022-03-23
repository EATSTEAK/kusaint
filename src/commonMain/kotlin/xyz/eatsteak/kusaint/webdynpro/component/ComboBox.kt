package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.state.State
import xyz.eatsteak.kusaint.webdynpro.app.WebDynProApplication
import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfCardinality
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfDelay
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData
import xyz.eatsteak.kusaint.webdynpro.parser.ComboBoxParser

@kotlinx.serialization.Serializable
class ComboBox(override val id: String) : Component {

    fun select(key: String) = EventBuilder("ComboBox", "Select") {
        parameter("Id", id)
        parameter("Key", key)
        parameter("ByEnter", "false")

        ucfParameter {
            responseData = UcfResponseData.DELTA
            action = UcfAction.SUBMIT
        }
    }

    fun change(value: String) = EventBuilder("ComboBox", "Change") {
        parameter("Id", id)
        parameter("Value", value)

        ucfParameter {
            responseData = UcfResponseData.DELTA
            enqueueCardinality = UcfCardinality.SINGLE
            delay = UcfDelay.FULL
        }
    }

    fun parser() = ComboBoxParser(this)
}

fun WebDynProApplication.selectOption(comboBox: ComboBox, key: String) = actionWithFormRequest {
    add(comboBox.select(key))
}

fun WebDynProApplication.selectOptionWithNotify(comboBox: ComboBox, key: String) = actionWithNotify {
    add(comboBox.select(key))
}

fun WebDynProApplication.selectTab(tabStrip: TabStrip, item: TabStrip.Item) = actionWithFormRequest {
    add(tabStrip.select(item))
}

fun WebDynProApplication.pressButtonWithChange(comboBox: ComboBox, value: String, button: Button) = actionWithNotify {
    add(comboBox.change(value))
    add(button.press())
}

fun WebDynProApplication.pressButtonWithOption(comboBox: ComboBox, key: String, button: Button) = actionWithNotify {
    add(comboBox.select(key))
    add(button.press())
}

suspend inline fun WebDynProApplication.findOptionAndPress(state: State<String>, comboBox: ComboBox, option: String, button: Button) {
    val options = comboBox.parser().parse(state)
    state.mutate(
        actionWithNotify {
            add(comboBox.select(options[option] ?: throw IllegalArgumentException("Cannot find option. possible values: $options")))
            add(button.press())
        }
    )
}

suspend inline fun WebDynProApplication.findOptionAndSelect(state: State<String>, comboBox: ComboBox, option: String) {
    val options = comboBox.parser().parse(state)
    state.mutate(
        actionWithFormRequest {
            add(comboBox.select(options[option] ?: throw IllegalArgumentException("Cannot find option. possible values: $options")))
        }
    )
}

suspend inline fun WebDynProApplication.findOptionAndEach(
    state: State<String>,
    comboBox: ComboBox,
    block: (option: String, optionKey: String) -> Unit
) {
    val options = comboBox.parser().parse(state)
    options.forEach { block(it.key, it.value) }
}