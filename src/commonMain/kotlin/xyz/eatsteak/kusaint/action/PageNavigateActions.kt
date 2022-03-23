package xyz.eatsteak.kusaint.action

import xyz.eatsteak.kusaint.webdynpro.app.WebDynProApplication

class WebDynProAppNavigateAction(val app: WebDynProApplication): PageNavigateAction(app.appUrl)