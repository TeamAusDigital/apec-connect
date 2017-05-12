package org.ausdigital.apecconnect

import com.mohiva.play.silhouette.api.Silhouette
import org.ausdigital.apecconnect.participants.model.ParticipantAuthEnv

package object auth {

  type ApecConnectSilhouette = Silhouette[ParticipantAuthEnv]
}
