package com.gamiphy.library

/**
 * A listener for Gamiphy bot redeem actions.
 */
interface OnRedeemTrigger {

    /**
     * Triggered whenever a redeem action fired in Gamiphy bot -when redeem clicked in the bot-.
     * @param rewardId: the reward object want to redeem.
     */
    fun onRedeemTrigger(rewardId: String)
}