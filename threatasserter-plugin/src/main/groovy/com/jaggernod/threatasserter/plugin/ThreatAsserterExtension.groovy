package com.jaggernod.threatasserter.plugin

class ThreatAsserterExtension {
  def enabled = true

  def setEnabled(boolean enabled) {
    this.enabled = enabled
  }

  def getEnabled() {
    return enabled
  }
}
