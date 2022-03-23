package xyz.eatsteak.kusaint.webdynpro.component

import xyz.eatsteak.kusaint.webdynpro.eventqueue.EventBuilder
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfAction
import xyz.eatsteak.kusaint.webdynpro.eventqueue.model.UcfResponseData
import kotlinx.serialization.Serializable

@Serializable
class TabStrip(override val id: String) : Component {
    fun select(item: Item) =
        EventBuilder("TabStrip", "TabSelect") {
            parameter("Id", id)
            parameter("ItemId", item.id)
            parameter("ItemIndex", item.index.toString())
            parameter("FirstVisibleItemIndex", item.firstVisibleItemIndex.toString())

            ucfParameter {
                responseData = UcfResponseData.DELTA
                action = UcfAction.SUBMIT
            }
        }

    @Serializable
    class Item(val id: String, val index: Int, val firstVisibleItemIndex: Int)
}